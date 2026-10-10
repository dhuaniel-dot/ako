package yunkang.ako.ui.pedido

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import yunkang.ako.databinding.ItemCabeceraSeccionBinding

// [Claude] 5a · la cabecera de una sección («CARNES»): un adaptador de un solo elemento
// la carta pega, por cada sección, una cabecera y un FilaPlatoAdapter con sus platos (ConcatAdapter)
class CabeceraAdapter(
    private val nombre: String
) : RecyclerView.Adapter<CabeceraAdapter.CabeceraViewHolder>() {

    class CabeceraViewHolder(val binding: ItemCabeceraSeccionBinding) : RecyclerView.ViewHolder(binding.root)

    override fun getItemCount(): Int = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CabeceraViewHolder {
        val binding = ItemCabeceraSeccionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CabeceraViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CabeceraViewHolder, position: Int) {
        // las mayúsculas las pone el XML (textAllCaps): el nombre se guarda tal cual
        holder.binding.textoCabecera.text = nombre
    }
}