package yunkang.ako.ui.resumen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import yunkang.ako.R
import yunkang.ako.databinding.ItemComandaCobradaBinding
import yunkang.ako.dominio.modelos.ComandaConTotal
import yunkang.ako.ui.comun.Formato

// 2g · las comandas cobradas de un día: «Mesa 9 · Cobrada a las 14:32 · 93,00 €», una fila por cobro
// propio, porque una comanda cobrada no es una línea (LineaAdapter). sencillo, como FilaPlatoAdapter:
// la lista solo cambia entera, al cambiar de día
class ComandaCobradaAdapter(
    // al tocar una fila: abrir su recibo
    private val alTocar: (ComandaConTotal) -> Unit
) : RecyclerView.Adapter<ComandaCobradaAdapter.FilaViewHolder>() {

    private var comandas: List<ComandaConTotal> = emptyList()

    class FilaViewHolder(val binding: ItemComandaCobradaBinding) : RecyclerView.ViewHolder(binding.root)

    // le dan las comandas del día (ya en orden de cobro)
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

        // «Mesa 9» (el número que ve el camarero, no el id), «Cobrada a las 14:32» y el importe (calculado)
        holder.binding.textoMesa.text = contexto.getString(R.string.comun_mesa, comanda.mesaNumero)
        holder.binding.textoHora.text =
            contexto.getString(R.string.resumen_fila_hora, Formato.hora(comanda.fechaCierre))
        holder.binding.textoImporte.text =
            contexto.getString(R.string.comun_precio, Formato.precio(comanda.totalCentimos))

        // toda la fila se puede tocar (≥ 48 dp): abre el recibo de esa comanda
        holder.binding.root.setOnClickListener { alTocar(comanda) }
    }
}