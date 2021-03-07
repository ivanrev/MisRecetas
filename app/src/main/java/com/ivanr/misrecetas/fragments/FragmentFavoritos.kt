package com.ivanr.misrecetas.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ivanr.misrecetas.R
import com.ivanr.misrecetas.adapters.RecetasAdapter
import com.ivanr.misrecetas.clases.Receta
import com.ivanr.misrecetas.listener.OnClick_ListReceta_Listener
import com.ivanr.misrecetas.utilidades.AdminSQLite
import com.ivanr.misrecetas.utilidades.Parametros

class FragmentFavoritos : Fragment(), OnClick_ListReceta_Listener {
    private var columnCount = 1
    private val rParam = Parametros
    private lateinit var lvRecetas_rec: RecyclerView
    lateinit var recetasAdapter: RecetasAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_main_ultimas, container, false)
        lvRecetas_rec = view.findViewById(R.id.recycler_recetas)
        lvRecetas_rec.layoutManager = when {
            columnCount <= 1 -> LinearLayoutManager(context)
            else -> GridLayoutManager(context, columnCount)
        }
        consultarRecetas(view.context)

        return view
    }

    companion object {
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
                FragmentFavoritos().apply {
                    arguments = Bundle().apply {
                    }
                }
    }

    fun consultarRecetas (p_context: Context) {
        val admin = AdminSQLite(p_context, "recetas", null, rParam.VERSION_BD)
        var fila = admin.consultar(admin, "select codigo, descripcion, elaboracion, ingredientes, favorito, url, foto from recetas where favorito = 'S' order by codigo desc")
        var listaRecetas = admin.carga_lista_recetas (fila, 999)
        recetasAdapter = RecetasAdapter(listaRecetas, this)
        lvRecetas_rec.adapter = recetasAdapter
    }

    override fun onClick_listRecetas(receta: Receta, position: Int, p_accion: String) {
        TODO("Not yet implemented")
    }

}