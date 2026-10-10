package yunkang.ako.ui.comun

import android.content.Context
import androidx.fragment.app.FragmentManager
import yunkang.ako.R
import yunkang.ako.dominio.modelos.PlatoConMesas

// los avisos de las mesas afectadas (ninguna línea se quita): montan el texto y abren la caja de siempre (ConfirmacionDialog)
// 2e al eliminar una categoría (Panel) y 3d al eliminar un plato (formulario). vive en comun porque lo usan dos pantallas
// [Claude] es un object (no guarda nada): solo sabe escribir los avisos
object MesasAfectadasDialog {

    // [Claude] el nombre del sobre que deja la caja (RESPUESTA_AFIRMATIVA true = Eliminar, false = Cancelar)
    const val CLAVE_CATEGORIA = "eliminar_categoria"
    const val CLAVE_PLATO = "eliminar_plato"

    // «Este plato está en comandas pendientes: Flan (mesa 5). No se quitará de esas comandas.»
    // todas las palabras salen de strings.xml; aquí solo se juntan las piezas
    private fun textoCategoria(contexto: Context, afectados: List<PlatoConMesas>): String {
        val y = contexto.getString(R.string.comun_y)
        // cada plato con sus mesas: «Flan (mesa 5)» o «Helado (mesas 5 y 7)»
        val partes = afectados.map { plato ->
            val mesas = Formato.lista(plato.mesas.map { it.toString() }, y)
            contexto.resources.getQuantityString(
                R.plurals.categoria_eliminar_plato_mesas, plato.mesas.size, plato.nombre, mesas
            )
        }
        // la frase entera, en singular o plural según cuántos platos haya
        return contexto.resources.getQuantityString(
            R.plurals.categoria_eliminar_cuerpo, afectados.size, partes.joinToString(", ")
        )
    }

    // un solo aviso con todos los platos y una única confirmación (ficha 2: cinco avisos seguidos no se leen)
    fun abrirCategoria(gestor: FragmentManager, contexto: Context, afectados: List<PlatoConMesas>) {
        ConfirmacionDialog.nueva(
            titulo = contexto.getString(R.string.categoria_eliminar_titulo),
            texto = textoCategoria(contexto, afectados),
            afirmativo = contexto.getString(R.string.comun_eliminar),
            negativo = contexto.getString(R.string.comun_cancelar),
            clave = CLAVE_CATEGORIA
        ).show(gestor, CLAVE_CATEGORIA)
    }

    // 3d · «Pollo asado está en una comanda pendiente de la mesa 6. No se quitará de esa comanda.»
    // una mesa o varias: el plurals elige la frase según cuántas mesas haya
    private fun textoPlato(contexto: Context, nombre: String, mesas: List<Int>): String {
        val y = contexto.getString(R.string.comun_y)
        val listaMesas = Formato.lista(mesas.map { it.toString() }, y)
        return contexto.resources.getQuantityString(
            R.plurals.mesas_afectadas_cuerpo, mesas.size, nombre, listaMesas
        )
    }

    // la misma caja que abrirCategoria, con el texto de un solo plato
    fun abrirPlato(gestor: FragmentManager, contexto: Context, nombre: String, mesas: List<Int>) {
        ConfirmacionDialog.nueva(
            titulo = contexto.getString(R.string.mesas_afectadas_titulo),
            texto = textoPlato(contexto, nombre, mesas),
            afirmativo = contexto.getString(R.string.comun_eliminar),
            negativo = contexto.getString(R.string.comun_cancelar),
            clave = CLAVE_PLATO
        ).show(gestor, CLAVE_PLATO)
    }
}