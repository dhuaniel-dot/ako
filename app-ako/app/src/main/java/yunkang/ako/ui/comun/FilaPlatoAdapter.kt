package yunkang.ako.ui.comun

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import yunkang.ako.R
import yunkang.ako.databinding.ItemFilaPlatoBinding
import yunkang.ako.datos.entidades.Producto
import java.io.File

// las filas de plato del Panel (2a) y de la carta (5a): «se reutiliza el componente, no la pantalla»
// adaptador sencillo con notifyDataSetChanged: la lista es corta y el atenuado depende también
// de la categoría, que DiffUtil no vería (Flan no cambia cuando se elimina Postres)
class FilaPlatoAdapter(
    // [Claude] qué hacer al tocar una fila: lo decide quien usa el adaptador, no el adaptador
    private val alTocar: (Producto) -> Unit
) : RecyclerView.Adapter<FilaPlatoAdapter.FilaViewHolder>() {

    private var platos: List<Producto> = emptyList()
    private var categoriaActiva: Boolean = true

    class FilaViewHolder(val binding: ItemFilaPlatoBinding) : RecyclerView.ViewHolder(binding.root)

    // la caja le da sus platos y si su categoría está en la carta
    fun mostrar(platos: List<Producto>, categoriaActiva: Boolean) {
        this.platos = platos
        this.categoriaActiva = categoriaActiva
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = platos.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilaViewHolder {
        val binding = ItemFilaPlatoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FilaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FilaViewHolder, position: Int) {
        val plato = platos[position]
        val contexto = holder.itemView.context

        // «12 · Entrecot» y «18,50 €»: los textos salen de strings.xml; el precio, de Formato
        holder.binding.textoNumeroNombre.text =
            contexto.getString(R.string.comun_plato_numero_nombre, plato.numero, plato.nombre)
        holder.binding.textoPrecio.text =
            contexto.getString(R.string.comun_precio, Formato.precio(plato.precioCentimos))

        pintarFoto(holder.binding.imagenPlato, plato.imagen?.let { File(it) }, plato.nombre)

        // la palabra «Eliminado» es la información; el gris solo la refuerza
        // en la carta nunca llega un plato eliminado, así que allí no sale sola
        holder.binding.textoEliminado.visibility = if (plato.activo) View.GONE else View.VISIBLE

        // atenuada si el plato está eliminado o si lo está su categoría (Flan dentro de Postres eliminada)
        holder.itemView.alpha = if (plato.activo && categoriaActiva) 1f else ALFA_ELIMINADO

        holder.itemView.setOnClickListener { alTocar(plato) }
    }

    companion object {
        // lo eliminado se ve apagado: se nota, pero la palabra «Eliminado» se sigue leyendo (contraste 5,15:1)
        const val ALFA_ELIMINADO = 0.8f
    }
}