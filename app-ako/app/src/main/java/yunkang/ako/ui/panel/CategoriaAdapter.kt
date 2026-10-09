package yunkang.ako.ui.panel

import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import yunkang.ako.R
import yunkang.ako.databinding.ItemCategoriaCajaBinding
import yunkang.ako.datos.entidades.Categoria
import yunkang.ako.datos.entidades.Producto
import yunkang.ako.dominio.modelos.CategoriaConPlatos
import yunkang.ako.ui.comun.FilaPlatoAdapter

// 2a · El encargado de las cajas del Panel (la fila de categorías de la carta tiene el suyo).
// ListAdapter: le das la lista nueva con submitList y DiffUtil repinta solo lo que cambió
class CategoriaAdapter(
    private val estaPlegada: (Long) -> Boolean,
    private val alPlegar: (Categoria) -> Unit,
    // [Claude] Los tres timbres de la caja: lo que pasa al tocarlos lo decide el Panel
    private val alEditar: (Categoria) -> Unit,
    private val alAnadirPlato: (Categoria) -> Unit,
    private val alTocarPlato: (Producto) -> Unit
) : ListAdapter<CategoriaConPlatos, CategoriaAdapter.CajaViewHolder>(Comparador) {

    // El armario de bandejas de plato compartido por todas las cajas: la que sobra en una la aprovecha otra
    private val armario = RecyclerView.RecycledViewPool()

    // Su vista y su encargado de filas, creado una sola vez
    class CajaViewHolder(
        val binding: ItemCategoriaCajaBinding,
        val filas: FilaPlatoAdapter
    ) : RecyclerView.ViewHolder(binding.root) {
        // [Claude] De qué categoría es ahora esta bandeja (0 = de ninguna todavía)
        var categoriaId: Long = 0L
    }

    // Pocas veces: fabrica una caja vacía con su lista de platos ya montada
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CajaViewHolder {
        val binding = ItemCategoriaCajaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val filas = FilaPlatoAdapter(alTocarPlato)
        binding.listaPlatos.layoutManager = LinearLayoutManager(parent.context)
        binding.listaPlatos.adapter = filas
        binding.listaPlatos.setRecycledViewPool(armario)
        // Dos listas que se mueven en vertical. Sin esto, la de fuera (el Panel) se queda el dedo
        // y la de dentro nunca baja. Mientras a la lista de platos le quede recorrido hacia donde va el dedo,
        // le pide al Panel que no se lo quite; al llegar al final, se lo deja (y el Panel sigue bajando).
        // Devuelve false: la lista sigue recibiendo el toque como siempre.
        // Fuente: https://developer.android.com/develop/ui/views/touch-and-input/gestures/viewgroup
        binding.listaPlatos.addOnItemTouchListener(object : RecyclerView.SimpleOnItemTouchListener() {
            private var yAnterior = 0f

            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                when (e.actionMasked) {
                    // El dedo toca: si la lista puede moverse (tiene más platos de los que caben), lo pide
                    MotionEvent.ACTION_DOWN -> {
                        yAnterior = e.y
                        rv.parent.requestDisallowInterceptTouchEvent(rv.canScrollVertically(1) || rv.canScrollVertically(-1))
                    }
                    // El dedo se mueve: ¿le queda recorrido hacia ese lado? Si no, el Panel puede quedárselo
                    MotionEvent.ACTION_MOVE -> {
                        val haciaArriba = e.y < yAnterior
                        val puede = if (haciaArriba) rv.canScrollVertically(1) else rv.canScrollVertically(-1)
                        rv.parent.requestDisallowInterceptTouchEvent(puede)
                        yAnterior = e.y
                    }
                }
                return false
            }
        })
        return CajaViewHolder(binding, filas)
    }

    // Cada vez que una caja entra en pantalla: se rellena con su categoría y sus platos
    override fun onBindViewHolder(holder: CajaViewHolder, position: Int) {
        val caja = getItem(position)
        val categoria = caja.categoria
        holder.binding.textoNombre.text = categoria.nombre

        // RNF-13: la etiqueta «Categoría eliminada» es la información; el gris del nombre solo la refuerza.
        // Sus platos salen atenuados sin la palabra «Eliminado» (siguen activos: R15 solo los quita de la carta)
        holder.binding.textoCategoriaEliminada.visibility = if (categoria.activo) View.GONE else View.VISIBLE
        holder.binding.textoNombre.alpha = if (categoria.activo) 1f else 0.8f

        // [Claude] Si la bandeja viene reciclada de OTRA categoría, traería su scroll: se sube arriba
        if (holder.categoriaId != categoria.id) {
            holder.binding.listaPlatos.scrollToPosition(0)
            holder.categoriaId = categoria.id
        }

        holder.filas.mostrar(caja.platos, categoria.activo)

        // Plegada = solo la cabecera (sin lista ni «+ Plato»); es la única excepción a la altura fija.
        // La flecha: ⌄ desplegada, › plegada
        val plegada = estaPlegada(categoria.id)
        val visibilidad = if (plegada) View.GONE else View.VISIBLE
        holder.binding.listaPlatos.visibility = visibilidad
        holder.binding.botonMasPlato.visibility = visibilidad
        holder.binding.botonPlegar.rotation = if (plegada) -90f else 0f
        // Con el nombre de la caja, para que el lector distinga una de otra («Plegar Postres»)
        holder.binding.botonPlegar.contentDescription = holder.itemView.context.getString(
            if (plegada) R.string.panel_desplegar_categoria_cd else R.string.panel_plegar_categoria_cd,
            categoria.nombre
        )
        // Tocar el icono o el nombre pliega o despliega; después se repinta solo esta caja
        val plegar = View.OnClickListener {
            alPlegar(categoria)
            notifyItemChanged(holder.bindingAdapterPosition)
        }
        holder.binding.botonPlegar.setOnClickListener(plegar)
        holder.binding.textoNombre.setOnClickListener(plegar)
        holder.binding.botonEditar.contentDescription =
            holder.itemView.context.getString(R.string.panel_editar_categoria_cd, categoria.nombre)
        holder.binding.botonEditar.setOnClickListener { alEditar(categoria) }
        holder.binding.botonMasPlato.setOnClickListener { alAnadirPlato(categoria) }
    }

    // DiffUtil: cómo comparar la foto vieja con la nueva
    object Comparador : DiffUtil.ItemCallback<CategoriaConPlatos>() {
        // ¿Es la misma caja? Se mira el id de la categoría (el nombre puede cambiar)
        override fun areItemsTheSame(vieja: CategoriaConPlatos, nueva: CategoriaConPlatos): Boolean =
            vieja.categoria.id == nueva.categoria.id

        // ¿Ha cambiado algo dentro? Una data class compara todos sus campos con ==
        override fun areContentsTheSame(vieja: CategoriaConPlatos, nueva: CategoriaConPlatos): Boolean =
            vieja == nueva
    }
}