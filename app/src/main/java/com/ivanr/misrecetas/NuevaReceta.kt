package com.ivanr.misrecetas

import android.content.ContentValues
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.fragment.findNavController

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class NuevaReceta : Fragment() {
    val util = Util()
    val fragContext = requireContext()
    var etDescripcion: TextView = TODO()
    var etIndicaciones: TextView = TODO()
    var etUrl : TextView = TODO()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.button_third).setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }
        etDescripcion = view?.findViewById(R.id.etDescripcion)
        etIndicaciones = view?.findViewById(R.id.etIndicaciones)
        etUrl = view?.findViewById(R.id.etUrl)
    }

    fun crearArticulo () {
        val admin = AdminSQLite(fragContext,"recetas", null, 1)
        val bd = admin.writableDatabase
        val receta = ContentValues()
        receta.put("descripcion", etDescripcion.getText().toString())
        receta.put("indicaciones", etIndicaciones.getText().toString())
        receta.put("url", etUrl.getText().toString())
        bd.insert("recetas", null, receta)
        bd.close()
        //et1.setText("")
        //et2.setText("")
        //et3.setText("")
        util.mensaje (fragContext,"Se cargaron los datos del artículo")

    }
}