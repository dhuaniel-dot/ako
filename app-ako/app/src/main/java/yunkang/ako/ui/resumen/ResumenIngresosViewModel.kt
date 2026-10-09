package yunkang.ako.ui.resumen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.launch
import yunkang.ako.EntradaAko
import yunkang.ako.datos.repositorios.ComandaRepository
import yunkang.ako.dominio.modelos.ComandaConTotal
import yunkang.ako.dominio.modelos.ResumenIngresos
import yunkang.ako.ui.comun.LineaVista
import java.time.LocalDate

// La libreta del Resumen de ingresos (2g): qué día se mira y qué se cobró ese día.
// Solo habla con el puesto de comandas (ComandaRepository), nunca con un DAO. Solo lee: no escribe nada
class ResumenIngresosViewModel(
    private val comandaRepository: ComandaRepository
) : ViewModel() {

    // El día que se mira; empieza en hoy (ficha 2g). Vive en la libreta: si Android rehace la pantalla
    // (por ejemplo, al pasar a modo noche), se sigue mirando el mismo día. Solo la libreta lo cambia (_dia)
    private val _dia = MutableLiveData(LocalDate.now())
    val dia: LiveData<LocalDate> = _dia

    // Lo cobrado el día que se mira. switchMap vigila el día: cada vez que cambia, tira la consulta
    // anterior y lanza otra con liveData { }, que pide el resumen a la base de datos y lo cuelga en el tablón.
    // Room cambia de hilo solo dentro de sus consultas
    val resumen: LiveData<ResumenIngresos> = dia.switchMap { elegido ->
        liveData { emit(comandaRepository.resumenDelDia(elegido)) }
    }

    // El calendario cambia el día; el resumen se vuelve a pedir solo, por el switchMap de arriba
    fun elegirDia(dia: LocalDate) {
        _dia.value = dia
    }

    // El recibo en solo lectura: la comanda cobrada que se mira (su mesa y su total, ya calculados por
    // resumenDelDia) y sus líneas tal como se pintan. Solo la libreta los cambia (_lineasRecibo)
    var comandaRecibo: ComandaConTotal? = null
        private set
    private val _lineasRecibo = MutableLiveData<List<LineaVista>>(emptyList())
    val lineasRecibo: LiveData<List<LineaVista>> = _lineasRecibo

    // Como CuentaViewModel.abrirComanda. Primero se vacía la bandeja, para que no se vea ni un instante
    // el recibo anterior; después se piden las líneas. Room cambia de hilo solo.
    // R14: nombre y precio salen de la línea, congelados al enviar, no de la carta de hoy
    fun abrirRecibo(comanda: ComandaConTotal) {
        comandaRecibo = comanda
        _lineasRecibo.value = emptyList()
        viewModelScope.launch {
            val lineas = comandaRepository.lineasDe(comanda.comandaId)
            // [Claude] Si mientras se esperaba a la base de datos se tocó otra comanda, estas líneas ya no valen
            if (comandaRecibo != comanda) return@launch
            _lineasRecibo.value = lineas.map { LineaVista.de(it) }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as EntradaAko
                ResumenIngresosViewModel(app.comandaRepository)
            }
        }
    }
}