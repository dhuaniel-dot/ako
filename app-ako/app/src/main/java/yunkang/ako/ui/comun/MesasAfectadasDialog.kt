package yunkang.ako.ui.comun

import android.content.Context
import androidx.fragment.app.FragmentManager
import yunkang.ako.R
import yunkang.ako.dominio.modelos.PlatoConMesas

// 2e · El aviso agrupado de R6 (P53 A): monta el texto y abre la caja de siempre (ConfirmacionDialog, P84).
// [Claude] Es un object (no guarda nada): solo sabe escribir el aviso. En la S7 crecerá con el del plato (3d)
object MesasAfectadasDialog {

    // [Claude] El nombre del sobre que deja la caja (RESPUESTA_AFIRMATIVA true = Eliminar, false = Cancelar)
    const val CLAVE_CATEGORIA = "eliminar_categoria"

    // «Este plato está en comandas pendientes: Flan (mesa 5). No se quitará de esas comandas.»
    // Todas las palabras salen de strings.xml; aquí solo se juntan las piezas
    fun textoCategoria(contexto: Context, afectados: List<PlatoConMesas>): String {
        val y = contexto.getString(R.string.comun_y)
        // Cada plato con sus mesas: «Flan (mesa 5)» o «Helado (mesas 5 y 7)»
        val partes = afectados.map { plato ->
            val mesas = Formato.lista(plato.mesas.map { it.toString() }, y)
            contexto.resources.getQuantityString(
                R.plurals.categoria_eliminar_plato_mesas, plato.mesas.size, plato.nombre, mesas
            )
        }
        // La frase entera, en singular o plural según cuántos platos haya
        return contexto.resources.getQuantityString(
            R.plurals.categoria_eliminar_cuerpo, afectados.size, partes.joinToString(", ")
        )
    }

    // Un solo aviso con todos los platos y una única confirmación (ficha 2: cinco avisos seguidos no se leen)
    fun abrirCategoria(gestor: FragmentManager, contexto: Context, afectados: List<PlatoConMesas>) {
        ConfirmacionDialog.nueva(
            titulo = contexto.getString(R.string.categoria_eliminar_titulo),
            texto = textoCategoria(contexto, afectados),
            afirmativo = contexto.getString(R.string.comun_eliminar),   // P40: el botón dice lo que hace
            negativo = contexto.getString(R.string.comun_cancelar),
            clave = CLAVE_CATEGORIA
        ).show(gestor, "mesas_afectadas")
    }
}