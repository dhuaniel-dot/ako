package yunkang.ako.ui.resumen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import yunkang.ako.R
import yunkang.ako.databinding.ItemComandaCobradaBinding
import yunkang.ako.dominio.modelos.ComandaConTotal
import yunkang.ako.ui.comun.Formato

// 2g · Las comandas cobradas de un día: «Mesa 9 · Cobrada a las 14:32 · 93,00 €», una fila por cobro.
// P86 A: propio, porque una comanda cobrada no es una línea (LineaAdapter); sencillo, con notifyDataSetChanged,
// como los demás adaptadores: la lista solo cambia entera, al cambiar de día
class ComandaCobradaAdapter(
    // Qué hacer al tocar una fila (abrir su recibo, pieza 7): lo decide quien usa el adaptador
    private val alTocar: (ComandaConTotal) -> Unit
) : RecyclerView.Adapter<ComandaCobradaAdapter.FilaViewHolder>() {

    private var comandas: List<ComandaConTotal> = emptyList()

    // La bandeja de una fila
    class FilaViewHolder(val binding: ItemComandaCobradaBinding) : RecyclerView.ViewHolder(binding.root)

    // Le dan las comandas del día (ya en orden de cobro, P126); se repinta todo
    fun mostrar(comandas: List<ComandaConTotal>) {
        this.comandas = comandas
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = comandas.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilaViewHolder {
        val binding = ItemComandaCobradaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FilaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FilaViewHolder, position: Int) {
        val comanda = comandas[position]
        val contexto = holder.itemView.context

        // «Mesa 9» (el número que ve el camarero, no el id), «Cobrada a las 14:32» y el importe (R10: calculado)
        holder.binding.textoMesa.text = contexto.getString(R.string.comun_mesa, comanda.mesaNumero)
        holder.binding.textoHora.text =
            contexto.getString(R.string.resumen_fila_hora, Formato.hora(comanda.fechaCierre))
        holder.binding.textoImporte.text =
            contexto.getString(R.string.comun_precio, Formato.precio(comanda.totalCentimos))

        // Toda la fila se puede tocar (≥ 48 dp): abre el recibo de esa comanda (leyenda 02g #2)
        holder.binding.root.setOnClickListener { alTocar(comanda) }
    }
}