package yunkang.ako.ui.comun

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.recyclerview.widget.GridLayoutManager
import yunkang.ako.R
import yunkang.ako.databinding.FragmentRejillaMesasBinding
import yunkang.ako.dominio.modelos.MesaEstado

// la rejilla de mesas: 1d (elegir mesa para pedir) y 6a (Cuenta). es el mismo componente
// pinta lo que le da quien la aloja (mostrar) y avisa de la mesa tocada con un sobre; no decide nada
class RejillaMesasFragment : Fragment(R.layout.fragment_rejilla_mesas) {

    // al tocar una mesa, sobre para quien la aloja: el id (para la base de datos) y el número (para la barra)
    private val adaptador = MesaAdapter { estado ->
        setFragmentResult(
            CLAVE_MESA_TOCADA,
            bundleOf(MESA_ID to estado.mesa.id, MESA_NUMERO to estado.mesa.numero)
        )
    }

    // solo mientras hay vista: onDestroyView lo necesita para soltar el adaptador
    private var binding: FragmentRejillaMesasBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentRejillaMesasBinding.bind(view)
        this.binding = binding

        // el título llega en los arguments
        binding.textoTitulo.setText(requireArguments().getInt(ARG_TITULO))

        binding.listaMesas.layoutManager = GridLayoutManager(requireContext(), COLUMNAS)
        binding.listaMesas.adapter = adaptador

        // ← Atrás hace lo mismo que el Atrás del sistema [Claude]
        binding.botonAtras.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    // al quitar la vista, la lista suelta su adaptador y se olvida el binding (como en ReciboFragment)
    override fun onDestroyView() {
        binding?.listaMesas?.adapter = null
        super.onDestroyView()
        binding = null
    }

    // quien la aloja le da las mesas. el adaptador las guarda aunque la vista aún no exista
    fun mostrar(mesas: List<MesaEstado>) {
        adaptador.mostrar(mesas)
    }

    companion object {
        // [Claude] el nombre del sobre y de lo que lleva dentro
        const val CLAVE_MESA_TOCADA = "mesa_tocada"
        const val MESA_ID = "mesa_id"
        const val MESA_NUMERO = "mesa_numero"

        private const val ARG_TITULO = "titulo"

        // columnas de la rejilla; lo que no cabe, con scroll (wireframe 01d)
        private const val COLUMNAS = 4

        // la forma de crear una rejilla: se le da el título que enseña su barra
        fun nueva(titulo: Int): RejillaMesasFragment {
            val rejilla = RejillaMesasFragment()
            rejilla.arguments = bundleOf(ARG_TITULO to titulo)
            return rejilla
        }
    }
}