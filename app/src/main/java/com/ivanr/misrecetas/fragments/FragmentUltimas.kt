package com.ivanr.misrecetas.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.ivanr.misrecetas.R
import com.ivanr.misrecetas.actividades.ActividadDetalle
import com.ivanr.misrecetas.actividades.ActividadNuevaReceta
import com.ivanr.misrecetas.adapters.RecetasAdapter
import com.ivanr.misrecetas.clases.Receta
import com.ivanr.misrecetas.listener.OnClick_ListReceta_Listener
import com.ivanr.misrecetas.utilidades.AdminSQLite
import com.ivanr.misrecetas.utilidades.Parametros

class FragmentUltimas : Fragment(), OnClick_ListReceta_Listener {
    private val rParam = Parametros
    private var columnCount = 1
    private lateinit var lvRecetas_rec: RecyclerView
    lateinit var recetasAdapter: RecetasAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_main_ultimas, container, false)
        lvRecetas_rec = view.findViewById(R.id.recycler_recetas)
        lvRecetas_rec.layoutManager = when {
            columnCount <= 1 -> LinearLayoutManager(context)
            else -> GridLayoutManager(context, columnCount)
        }
        val fab: FloatingActionButton = view.findViewById(R.id.bt_anadir_receta)
        fab.setOnClickListener {
            startActivity(Intent(view.context, ActividadNuevaReceta::class.java))
        }

        consultarRecetas(view.context)

        return view
    }

    companion object {
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            FragmentUltimas().apply {
            }
    }

    fun consultarRecetas (p_context: Context) {
        val admin = AdminSQLite(p_context, "recetas", null, rParam.VERSION_BD)
        var fila = admin.consultar(admin, "select codigo, descripcion, elaboracion, ingredientes, favorito, url, foto, categoria, maquina_cocinado from recetas order by codigo desc")
        var listaRecetas = admin.carga_lista_recetas (fila, 999)
        recetasAdapter = RecetasAdapter(listaRecetas, this)
        lvRecetas_rec.adapter = recetasAdapter
    }

    override fun onClick_listRecetas(receta: Receta, position: Int, p_accion:String) {
        if (p_accion == "navegar") {
            navegar_detalle(receta)
        }
        else if (p_accion=="favorito") {
            actualiza_favorito(receta)
        }
    }

    fun actualiza_favorito (receta:Receta) {
        val admin = AdminSQLite(view?.context, "recetas", null, rParam.VERSION_BD)
        val v_campos = arrayOfNulls<String>(5)
        var v_valores = arrayOfNulls<String>(5)
        v_campos[0] = "favorito"

        if (receta.v_favorito == "N") {
            v_valores[0] = "S"
            admin.actualizar(admin, "recetas", receta.v_id,v_campos, v_valores)
            receta.v_favorito = "S"
            //vh.btFavorito.setImageResource(R.drawable.ic_corazon_lleno)
        } else {
            v_valores[0] = "N"
            admin.actualizar(admin, "recetas", receta.v_id, v_campos, v_valores)
            receta.v_favorito = "N"
            //vh.btFavorito.setImageResource(R.drawable.ic_corazon_vacio)
        }
        lvRecetas_rec.adapter!!.notifyDataSetChanged()
    }
    fun navegar_detalle(p_receta: Receta) {
        val intent = Intent(activity, ActividadDetalle::class.java)
        val bundle = Bundle()
        bundle.putInt("id_receta", p_receta.v_id)
        bundle.putString("descripcion_receta", p_receta.v_descripcion)
        bundle.putString("favorito_receta", p_receta.v_favorito)
        intent.putExtras(bundle)
        requireActivity().startActivity(intent)
    }

}