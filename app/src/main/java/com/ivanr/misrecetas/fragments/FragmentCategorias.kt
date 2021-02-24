package com.ivanr.misrecetas.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.ivanr.misrecetas.R
import com.ivanr.misrecetas.actividades.ActividadNuevaCategoria
import com.ivanr.misrecetas.actividades.ActividadNuevaReceta
import com.ivanr.misrecetas.ui.models.ViewModelCategorias

class FragmentCategorias : Fragment() {

    private lateinit var viewModelCategorias: ViewModelCategorias

    override fun onCreateView( inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View? {
        viewModelCategorias = ViewModelProvider(this).get(ViewModelCategorias::class.java)
        val root = inflater.inflate(R.layout.fragment_main_categorias, container, false)
        val fab: FloatingActionButton = root.findViewById(R.id.bt_anadir_receta)
        fab.setOnClickListener {
            startActivity(Intent(root.context, ActividadNuevaCategoria::class.java))
        }

        return root
    }
}