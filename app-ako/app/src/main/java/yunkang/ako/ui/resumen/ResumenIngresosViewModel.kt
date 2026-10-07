package yunkang.ako.ui.resumen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import yunkang.ako.EntradaAko
import yunkang.ako.datos.repositorios.ComandaRepository
import yunkang.ako.dominio.modelos.ResumenIngresos
import java.time.LocalDate

// La libreta del Resumen de ingresos (2g): qué día se mira y qué se cobró ese día.
// Solo habla con el puesto de comandas (ComandaRepository), nunca con un DAO (spec 3). Solo lee: no escribe nada
class ResumenIngresosViewModel(
    private val comandaRepository: ComandaRepository
) : ViewModel() {

    // El día que se mira; empieza en hoy (ficha 2g). Vive en la libreta: si Android rehace la pantalla
    // (por ejemplo, al pasar a modo noche), se sigue mirando el mismo día. Solo la libreta lo cambia (_dia)
    private val _dia = MutableLiveData(LocalDate.now())
    val dia: LiveData<LocalDate> = _dia

    // P195 C: lo cobrado el día que se mira. switchMap vigila el día: cada vez que cambia, tira la consulta
    // anterior y lanza otra con liveData { }, que pide el resumen a la base de datos y lo cuelga en el tablón.
    // liveData { } empieza en el hilo principal; Room cambia de hilo solo dentro de sus consultas
    val resumen: LiveData<ResumenIngresos> = dia.switchMap { elegido ->
        liveData { emit(comandaRepository.resumenDelDia(elegido)) }
    }

    // El calendario (pieza 6) cambia el día; el resumen se vuelve a pedir solo, por el switchMap de arriba
    fun elegirDia(dia: LocalDate) {
        _dia.value = dia
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