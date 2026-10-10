package yunkang.ako.ui.pedido

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import yunkang.ako.databinding.ItemCategoriaFilaBinding
import yunkang.ako.datos.entidades.Categoria
import yunkang.ako.ui.comun.pintarFoto
import java.io.File

// 5a · la fila de categorías de la carta: un círculo con el nombre por categoría, la activa resaltada
// no es el CategoriaAdapter del Panel: solo comparten el dato. sencillo, como FilaPlatoAdapter
class CategoriaFilaAdapter(
    // al tocar una categoría: saltar a su sección
    private val alTocar: (Long) -> Unit
) : RecyclerView.Adapter<CategoriaFilaAdapter.FilaViewHolder>() {

    private var categorias: List<Categoria> = emptyList()
    private var activaId: Long = 0L

    class FilaViewHolder(val binding: ItemCategoriaFilaBinding) : RecyclerView.ViewHolder(binding.root)

    // la carta le da sus categorías (solo las que tienen algún plato visible)
    fun mostrar(categorias: List<Categoria>) {
        this.categorias = categorias
        notifyDataSetChanged()
    }

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

        pintarFoto(holder.binding.imagenCategoria, categoria.imagen?.let { File(it) }, categoria.nombre, redonda = true)

        // la activa no se distingue solo por el color: negrita y raya debajo
        // isSelected hace que el lector de pantalla diga «seleccionado»
        holder.binding.textoNombre.setTypeface(null, if (activa) Typeface.BOLD else Typeface.NORMAL)
        holder.binding.rayaActiva.visibility = if (activa) View.VISIBLE else View.INVISIBLE
        holder.itemView.isSelected = activa

        holder.itemView.setOnClickListener { alTocar(categoria.id) }
    }
}