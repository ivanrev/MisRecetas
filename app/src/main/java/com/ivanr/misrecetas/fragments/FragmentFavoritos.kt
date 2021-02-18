package com.ivanr.misrecetas.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import com.ivanr.misrecetas.R
import com.ivanr.misrecetas.adapters.RecetasAdapter
import com.ivanr.misrecetas.utilidades.AdminSQLite

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentFavoritos.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentFavoritos : Fragment() {
    lateinit var lvRecetas : ListView
    lateinit var recetasAdapter: RecetasAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_ultimas, container, false)
        lvRecetas = view.findViewById(R.id.lvRecetas)
        consultarRecetas(view.context)

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FragmentFavoritos.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
                FragmentFavoritos().apply {
                    arguments = Bundle().apply {
                    }
                }
    }

    fun consultarRecetas (p_context: Context) {
        val admin = AdminSQLite(p_context, "recetas", null, 1)
        var fila = admin.consultar(admin, "select codigo, descripcion, elaboracion, ingredientes, favorito, url, foto from recetas where favorito = 'S' order by codigo desc")
        var listaRecetas = admin.carga_lista_recetas (fila)
        recetasAdapter = RecetasAdapter(p_context, listaRecetas)
        lvRecetas.adapter = recetasAdapter
    }
}