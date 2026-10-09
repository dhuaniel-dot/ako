package yunkang.ako.ui.pedido

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import yunkang.ako.R
import yunkang.ako.databinding.ItemCategoriaFilaBinding
import yunkang.ako.datos.entidades.Categoria
import java.io.File

// 5a · La fila de categorías de la carta: un círculo con el nombre por categoría, la activa resaltada.
// No es el CategoriaAdapter del Panel: solo comparten el dato. Sencillo, con notifyDataSetChanged
class CategoriaFilaAdapter(
    // Qué hacer al tocar una categoría (saltar a su sección): lo decide la carta, no el adaptador
    private val alTocar: (Long) -> Unit
) : RecyclerView.Adapter<CategoriaFilaAdapter.FilaViewHolder>() {

    private var categorias: List<Categoria> = emptyList()
    private var activaId: Long = 0L

    class FilaViewHolder(val binding: ItemCategoriaFilaBinding) : RecyclerView.ViewHolder(binding.root)

    // La carta le da sus categorías (solo las que tienen algún plato visible)
    fun mostrar(categorias: List<Categoria>) {
        this.categorias = categorias
        notifyDataSetChanged()
    }

    // Marca cuál es la activa
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
        val contexto = holder.itemView.context

        holder.binding.textoNombre.text = categoria.nombre

        // La foto redonda: Glide siempre, también sin foto (null → el «?»), porque las bandejas se reciclan
        Glide.with(holder.binding.imagenCategoria)
            .load(categoria.imagen?.let { File(it) })
            .placeholder(R.drawable.foto_cargando)
            .error(R.drawable.ic_sin_foto)
            .circleCrop()
            .into(holder.binding.imagenCategoria)
        // Sin foto, «Sin foto»; con foto, el nombre de la categoría
        if (categoria.imagen == null) {
            holder.binding.imagenCategoria.contentDescription = contexto.getString(R.string.comun_sin_foto_cd)
        } else {
            holder.binding.imagenCategoria.contentDescription = categoria.nombre
        }

        // RNF-13: la activa no se distingue solo por el color: negrita y raya debajo.
        // isSelected hace que el lector de pantalla diga «seleccionado»
        holder.binding.textoNombre.setTypeface(null, if (activa) Typeface.BOLD else Typeface.NORMAL)
        holder.binding.rayaActiva.visibility = if (activa) View.VISIBLE else View.INVISIBLE
        holder.itemView.isSelected = activa

        holder.itemView.setOnClickListener { alTocar(categoria.id) }
    }
}