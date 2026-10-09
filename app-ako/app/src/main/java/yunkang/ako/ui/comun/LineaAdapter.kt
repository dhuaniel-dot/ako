package yunkang.ako.ui.comun

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import yunkang.ako.R
import yunkang.ako.databinding.ItemLineaBinding
import yunkang.ako.dominio.Validacion

// Las líneas de un pedido: «2 × Entrecot · 37,00 €» y, debajo, los botones que toquen.
// Recibe LineaVista, no las clases del dominio, para servir al carrito (5c), a la comanda (6b) y al recibo (6c, 2g).
// Sin modos. Quien lo usa pasa solo las acciones que quiere; una acción vacía (null) esconde su botón:
// carrito − + Quitar · comanda solo Quitar · recibo ninguna (y entonces se esconde la fila de botones entera).
// Sencillo, con notifyDataSetChanged, como los demás adaptadores
class LineaAdapter(
    // Qué hacer con cada botón: lo decide quien usa el adaptador; null = ese botón no sale
    private val alMenos: ((LineaVista) -> Unit)?,
    private val alMas: ((LineaVista) -> Unit)?,
    private val alQuitar: ((LineaVista) -> Unit)?
) : RecyclerView.Adapter<LineaAdapter.LineaViewHolder>() {

    private var lineas: List<LineaVista> = emptyList()

    class LineaViewHolder(val binding: ItemLineaBinding) : RecyclerView.ViewHolder(binding.root)

    fun mostrar(lineas: List<LineaVista>) {
        this.lineas = lineas
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = lineas.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LineaViewHolder {
        val binding = ItemLineaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LineaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LineaViewHolder, position: Int) {
        val linea = lineas[position]
        val contexto = holder.itemView.context

        // «2 × Entrecot» y su importe (R10: precio × cantidad, calculado)
        holder.binding.textoLinea.text = contexto.getString(R.string.comun_linea, linea.cantidad, linea.nombre)
        holder.binding.textoImporte.text =
            contexto.getString(R.string.comun_precio, Formato.precio(linea.importeCentimos))

        // Cada botón sale solo si le dieron qué hacer; sin ninguno, fuera la fila entera (el recibo)
        holder.binding.botonMenos.isVisible = alMenos != null
        holder.binding.botonMas.isVisible = alMas != null
        holder.binding.botonQuitar.isVisible = alQuitar != null
        holder.binding.filaBotones.isVisible = alMenos != null || alMas != null || alQuitar != null

        // R4: − apagado en 1 y + apagado en 99; Quitar quita la línea entera
        holder.binding.botonMenos.isEnabled = linea.cantidad > 1
        holder.binding.botonMas.isEnabled = linea.cantidad < Validacion.MAXIMO_POR_PLATO
        holder.binding.botonMenos.setOnClickListener { alMenos?.invoke(linea) }
        holder.binding.botonMas.setOnClickListener { alMas?.invoke(linea) }
        holder.binding.botonQuitar.setOnClickListener { alQuitar?.invoke(linea) }

        // El lector de pantalla dice de qué plato es cada botón («Quitar uno de Entrecot»)
        holder.binding.botonMenos.contentDescription = contexto.getString(R.string.linea_menos_cd, linea.nombre)
        holder.binding.botonMas.contentDescription = contexto.getString(R.string.linea_mas_cd, linea.nombre)
        holder.binding.botonQuitar.contentDescription = contexto.getString(R.string.linea_quitar_cd, linea.nombre)
    }
}