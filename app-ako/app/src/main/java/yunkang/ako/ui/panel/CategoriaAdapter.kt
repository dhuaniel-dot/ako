package yunkang.ako.ui.panel

import android.view.LayoutInflater
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

// 2a · El encargado de las cajas del Panel (P52 A: la fila de categorías de la carta tendrá el suyo, S8).
// ListAdapter: le das la lista nueva con submitList y DiffUtil repinta solo lo que cambió (P50 C)
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

    // La bandeja de una caja: su vista y SU encargado de filas, creado una sola vez con la bandeja
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
        holder.binding.textoNombre.alpha = if (categoria.activo) 1f else 0.6f

        // [Claude] Si la bandeja viene reciclada de OTRA categoría, traería su scroll: se sube arriba
        if (holder.categoriaId != categoria.id) {
            holder.binding.listaPlatos.scrollToPosition(0)
            holder.categoriaId = categoria.id
        }

        holder.filas.mostrar(caja.platos, categoria.activo)

        // P56 A: plegada = solo la cabecera (sin lista ni «+ Plato»); es la única excepción a la altura fija
        val plegada = estaPlegada(categoria.id)
        val visibilidad = if (plegada) View.GONE else View.VISIBLE
        holder.binding.listaPlatos.visibility = visibilidad
        holder.binding.botonMasPlato.visibility = visibilidad
        holder.binding.botonPlegar.rotation = if (plegada) -90f else 0f   // ⌄ desplegada, › plegada
        holder.binding.botonPlegar.contentDescription = holder.itemView.context.getString(
            if (plegada) R.string.panel_desplegar_cd else R.string.panel_plegar_cd
        )
        // Tocar el icono o el nombre pliega o despliega; después se repinta solo esta caja
        val plegar = View.OnClickListener {
            alPlegar(categoria)
            notifyItemChanged(holder.bindingAdapterPosition)
        }
        holder.binding.botonPlegar.setOnClickListener(plegar)
        holder.binding.textoNombre.setOnClickListener(plegar)
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