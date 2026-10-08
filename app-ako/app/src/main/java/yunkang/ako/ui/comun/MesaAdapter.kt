package yunkang.ako.ui.comun

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import yunkang.ako.R
import yunkang.ako.databinding.ItemMesaBinding
import yunkang.ako.dominio.modelos.MesaEstado

// Las 60 mesas de la rejilla (1d elegir y 6a gestionar): convierte cada MesaEstado en una casilla.
// Adaptador sencillo con notifyDataSetChanged, como FilaPlatoAdapter [Claude]
class MesaAdapter(
    // Qué hacer al tocar una mesa: lo decide quien usa el adaptador, no el adaptador
    private val alTocar: (MesaEstado) -> Unit
) : RecyclerView.Adapter<MesaAdapter.MesaViewHolder>() {

    private var mesas: List<MesaEstado> = emptyList()

    // La bandeja de una casilla. [Claude] Al nacer apunta sus colores de mesa libre (los del tema,
    // claro u oscuro), para volver a ellos si la casilla se reutiliza para una mesa libre
    class MesaViewHolder(val binding: ItemMesaBinding) : RecyclerView.ViewHolder(binding.root) {
        val fondoLibre: ColorStateList = binding.tarjetaMesa.cardBackgroundColor
        val bordeLibre: ColorStateList? = binding.tarjetaMesa.strokeColorStateList
        val textoLibre: ColorStateList = binding.textoNumero.textColors
    }

    // La rejilla le da las mesas; se repinta todo
    fun mostrar(mesas: List<MesaEstado>) {
        this.mesas = mesas
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = mesas.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MesaViewHolder {
        val binding = ItemMesaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MesaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MesaViewHolder, position: Int) {
        val estado = mesas[position]
        val tarjeta = holder.binding.tarjetaMesa
        val contexto = holder.itemView.context
        val textoMesa = contexto.getString(R.string.comun_mesa, estado.mesa.numero)

        holder.binding.textoNumero.text = contexto.getString(R.string.comun_numero, estado.mesa.numero)

        // R3: ocupada = tiene comanda pendiente. No hay ninguna columna «ocupada»: lo dice el comandaId
        if (estado.comandaId != null) {
            // Roja, letras blancas y el total (R10: lo suma la base de datos, nadie lo guarda)
            val total = contexto.getString(R.string.comun_precio, Formato.precio(estado.totalCentimos ?: 0))
            val rojo = ContextCompat.getColor(contexto, R.color.mesa_ocupada)
            val blanco = ContextCompat.getColor(contexto, R.color.sobre_mesa_ocupada)
            tarjeta.setCardBackgroundColor(rojo)
            tarjeta.strokeColor = rojo   // el borde, del mismo rojo: no se ve
            holder.binding.textoNumero.setTextColor(blanco)
            holder.binding.textoTotal.setTextColor(blanco)
            holder.binding.textoTotal.text = total
            holder.binding.textoTotal.visibility = View.VISIBLE
            // RNF-13: el lector de pantalla dice «Mesa 4, 42,00 €»: el estado no depende solo del color.
            // La frase (con su coma) sale de strings.xml, como todo texto (RNF-19, H07)
            tarjeta.contentDescription = contexto.getString(R.string.mesa_ocupada_cd, textoMesa, total)
        } else {
            // Libre: vuelve a los colores con los que nació la casilla, sin total
            tarjeta.setCardBackgroundColor(holder.fondoLibre)
            tarjeta.setStrokeColor(holder.bordeLibre)
            holder.binding.textoNumero.setTextColor(holder.textoLibre)
            holder.binding.textoTotal.visibility = View.GONE
            tarjeta.contentDescription = textoMesa
        }

        // En modo elegir toda mesa se puede tocar, roja o blanca (ficha 1): el adaptador no decide nada
        tarjeta.setOnClickListener { alTocar(estado) }
    }
}