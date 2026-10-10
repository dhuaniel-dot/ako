package yunkang.ako.seguridad

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertFalse
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import yunkang.ako.datos.repositorios.SeguridadRepository

// El PIN con su archivo de verdad (las SharedPreferences "pin"), por eso va en androidTest.
// Es el mismo archivo que el de la app instalada: cada prueba lo vacía al empezar.
@RunWith(AndroidJUnit4::class)
class SeguridadRepositoryTest {

    private lateinit var repositorio: SeguridadRepository

    @Before
    fun empezarSinPin() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        context.getSharedPreferences("pin", Context.MODE_PRIVATE).edit().clear().commit()
        repositorio = SeguridadRepository(GuardaPin(context))
    }

    // Con el PIN actual mal, cambiarPin no guarda el nuevo: el de antes sigue valiendo
    @Test
    fun cambiarPinConActualMaloNoCambia() {
        runBlocking { repositorio.crearPin("1234") }
        val cambiado = runBlocking { repositorio.cambiarPin(actual = "0000", nuevo = "5678") }
        assertFalse(cambiado)
        assertTrue(runBlocking { repositorio.comprobarPin("1234") })
    }

    // Si ya hay PIN, crearPin no lo pisa: para cambiarlo hay que dar el actual
    @Test
    fun crearPinDosVecesLanza() {
        runBlocking { repositorio.crearPin("1234") }
        assertThrows(IllegalStateException::class.java) { runBlocking { repositorio.crearPin("5678") } }
    }
}
