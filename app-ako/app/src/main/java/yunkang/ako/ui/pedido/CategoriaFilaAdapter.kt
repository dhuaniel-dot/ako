package yunkang.ako.ui.pedido

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import yunkang.ako.databinding.ItemCategoriaFilaBinding
import yunkang.ako.datos.entidades.Categoria

// 5a · La fila de categorías de la carta: un círculo con el nombre por categoría, la activa resaltada.
// No es el CategoriaAdapter del Panel (P52 A): solo comparten el dato. Sencillo, con notifyDataSetChanged
class CategoriaFilaAdapter(
    // Qué hacer al tocar una categoría (saltar a su sección): lo decide la carta, no el adaptador
    private val alTocar: (Long) -> Unit
) : RecyclerView.Adapter<CategoriaFilaAdapter.FilaViewHolder>() {

    private var categorias: List<Categoria> = emptyList()
    private var activaId: Long = 0L

    // La bandeja de un círculo
    class FilaViewHolder(val binding: ItemCategoriaFilaBinding) : RecyclerView.ViewHolder(binding.root)

    // La carta le da sus categorías (solo las que tienen algún plato visible); se repinta todo
    fun mostrar(categorias: List<Categoria>) {
        this.categorias = categorias
        notifyDataSetChanged()
    }

    // Marca cuál es la activa; se repinta todo
    fun resaltar(categoriaId: Long) {
        activaId = categoriaId
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = categorias.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilaViewHolder {
        val binding = ItemCategoriaFilaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FilaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FilaViewHolder, position: Int) {
        val categoria = categorias[position]
        val activa = categoria.id == activaId

        holder.binding.textoNombre.text = categoria.nombre

        // RNF-13: la activa no se distingue solo por el color: negrita y raya debajo.
        // isSelected hace que el lector de pantalla diga «seleccionado»
        holder.binding.textoNombre.setTypeface(null, if (activa) Typeface.BOLD else Typeface.NORMAL)
        holder.binding.rayaActiva.visibility = if (activa) View.VISIBLE else View.INVISIBLE
        holder.itemView.isSelected = activa

        holder.itemView.setOnClickListener { alTocar(categoria.id) }
    }
}