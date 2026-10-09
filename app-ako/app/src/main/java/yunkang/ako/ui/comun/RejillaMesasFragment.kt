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

// La rejilla de 60 mesas: 1d (elegir mesa para pedir) y 6a (Cuenta). Es el mismo componente.
// Pinta lo que le da quien la aloja (mostrar) y avisa de la mesa tocada con un sobre; no decide nada
class RejillaMesasFragment : Fragment(R.layout.fragment_rejilla_mesas) {

    // Al tocar una mesa, sobre para quien la aloja: el id (para la base de datos) y el número (para la barra)
    private val adaptador = MesaAdapter { estado ->
        setFragmentResult(
            CLAVE_MESA_TOCADA,
            bundleOf(MESA_ID to estado.mesa.id, MESA_NUMERO to estado.mesa.numero)
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentRejillaMesasBinding.bind(view)

        // El título llega en los arguments (se conservan si Android rehace el Fragment)
        binding.textoTitulo.setText(requireArguments().getInt(ARG_TITULO))

        // 4 columnas: 60 mesas caben en 15 filas con scroll (wireframe 01d).
        binding.listaMesas.layoutManager = GridLayoutManager(requireContext(), 4)
        binding.listaMesas.adapter = adaptador

        // ← Atrás hace lo mismo que el Atrás del sistema [Claude]
        binding.botonAtras.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    // Al quitar la vista, la lista suelta su adaptador (como en ReciboFragment)
    override fun onDestroyView() {
        FragmentRejillaMesasBinding.bind(requireView()).listaMesas.adapter = null
        super.onDestroyView()
    }

    // Quien la aloja le da las mesas. El adaptador las guarda aunque la vista aún no exista
    fun mostrar(mesas: List<MesaEstado>) {
        adaptador.mostrar(mesas)
    }

    companion object {
        // [Claude] El nombre del sobre y de lo que lleva dentro
        const val CLAVE_MESA_TOCADA = "mesa_tocada"
        const val MESA_ID = "mesa_id"
        const val MESA_NUMERO = "mesa_numero"

        private const val ARG_TITULO = "titulo"

        // La forma de crear una rejilla: se le da el título que enseña su barra
        fun nueva(titulo: Int): RejillaMesasFragment {
            val rejilla = RejillaMesasFragment()
            rejilla.arguments = bundleOf(ARG_TITULO to titulo)
            return rejilla
        }
    }
}