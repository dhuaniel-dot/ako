package yunkang.ako.ui.comun

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import yunkang.ako.R
import yunkang.ako.databinding.ItemLineaBinding
import yunkang.ako.dominio.Validacion

// Las líneas del carrito (5c): «2 × Entrecot · 37,00 €» y debajo − + y Quitar.
// P68 A: recibe LineaVista, no las clases del dominio, para que en la S9 sirva también a las comandas.
// [Claude] Hoy solo existe el uso del carrito; los otros modos (solo Quitar, solo lectura) llegan con la S9.
// Sencillo, con notifyDataSetChanged, como los demás adaptadores
class LineaAdapter(
    // Qué hacer con cada botón: lo decide quien usa el adaptador
    private val alMenos: (LineaVista) -> Unit,
    private val alMas: (LineaVista) -> Unit,
    private val alQuitar: (LineaVista) -> Unit
) : RecyclerView.Adapter<LineaAdapter.LineaViewHolder>() {

    private var lineas: List<LineaVista> = emptyList()

    // La bandeja de una línea
    class LineaViewHolder(val binding: ItemLineaBinding) : RecyclerView.ViewHolder(binding.root)

    // Le dan las líneas; se repinta todo
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

        // R4: − apagado en 1 y + apagado en 99; Quitar quita la línea entera, sin preguntar (ficha 5)
        holder.binding.botonMenos.isEnabled = linea.cantidad > 1
        holder.binding.botonMas.isEnabled = linea.cantidad < Validacion.MAXIMO_POR_PLATO
        holder.binding.botonMenos.setOnClickListener { alMenos(linea) }
        holder.binding.botonMas.setOnClickListener { alMas(linea) }
        holder.binding.botonQuitar.setOnClickListener { alQuitar(linea) }
    }
}