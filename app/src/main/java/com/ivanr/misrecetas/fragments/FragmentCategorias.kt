package com.ivanr.misrecetas.fragments


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.ivanr.misrecetas.R
import com.ivanr.misrecetas.adapters.CategoriasAdapter
import com.ivanr.misrecetas.ui.models.ViewModelCategorias
import com.ivanr.misrecetas.utilidades.AdminSQLite
import com.ivanr.misrecetas.utilidades.Parametros

class FragmentCategorias : Fragment() {
    private var columnCount = 2
    lateinit var categoriasAdapter: CategoriasAdapter
    private var rParam = Parametros
    private lateinit var viewModelCategorias: ViewModelCategorias

    override fun onCreateView( inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View? {
        viewModelCategorias = ViewModelProvider(this).get(ViewModelCategorias::class.java)
        val root = inflater.inflate(R.layout.fragment_main_categorias, container, false)
        val recycler:RecyclerView = root.findViewById(R.id.recycler_categorias)

        recycler.layoutManager = when {
            columnCount <= 1 -> LinearLayoutManager(context)
            else -> GridLayoutManager(context, columnCount)
        }
        consultarCategorias (recycler)

        val fab: FloatingActionButton = root.findViewById(R.id.bt_anadir_categoria)
        fab.setOnClickListener {
            //startActivity(Intent(root.context, ActividadNuevaCategoria::class.java))
        }
        return root
    }

    fun consultarCategorias (recycler:RecyclerView) {
        val admin = AdminSQLite(context, "recetas", null, rParam.VERSION_BD)
        var fila = admin.consultar(admin, "select codigo, orden, descripcion, foto from categorias order by orden desc")
        var listaCategorias = admin.carga_lista_categorias (fila)
        categoriasAdapter = CategoriasAdapter(listaCategorias)
        recycler.adapter = categoriasAdapter
    }
}