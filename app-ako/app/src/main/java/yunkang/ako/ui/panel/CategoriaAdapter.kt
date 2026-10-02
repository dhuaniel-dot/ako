package yunkang.ako.ui.panel

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import yunkang.ako.databinding.ItemCategoriaCajaBinding
import yunkang.ako.dominio.modelos.CategoriaConPlatos

// 2a · El encargado de las cajas del Panel (P52 A: la fila de categorías de la carta tendrá el suyo, S8).
// ListAdapter: le das la lista nueva con submitList y DiffUtil repinta solo lo que cambió (P50 C)
class CategoriaAdapter : ListAdapter<CategoriaConPlatos, CategoriaAdapter.CajaViewHolder>(Comparador) {

    // La bandeja: la vista de una caja con sus huecos ya localizados (por ViewBinding)
    class CajaViewHolder(val binding: ItemCategoriaCajaBinding) : RecyclerView.ViewHolder(binding.root)

    // Se llama solo unas pocas veces: fabrica una bandeja vacía a partir del XML
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CajaViewHolder {
        val binding = ItemCategoriaCajaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CajaViewHolder(binding)
    }

    // Se llama cada vez que una bandeja entra en pantalla: la rellena con el dato de esa posición
    override fun onBindViewHolder(holder: CajaViewHolder, position: Int) {
        val caja = getItem(position)
        holder.binding.textoNombre.text = caja.categoria.nombre
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