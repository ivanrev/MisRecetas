package com.ivanr.misrecetas

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
abstract class UltimasRecetas : Fragment() {
    val util = Util()
    val fragContext = requireContext()
    var listaRecetas = ArrayList<Receta>()
    var lvRecetas : ListView = TODO()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.button_first).setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }

        lvRecetas = view?.findViewById(R.id.lvRecetas) as ListView
        lvRecetas.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, position, id ->
            util.mensaje(fragContext, "Receta: " + listaRecetas[position].descripcion)
        }

    }

    fun consultarRecetas () {

        val admin = AdminSQLite(fragContext, "recetas", null, 1)
        val bd = admin.writableDatabase
        var fila = bd.rawQuery(
            "select descripcion, indicaciones from recetas order by codigo desc",
            null
        )
        listaRecetas.clear()
        if (fila.moveToFirst()) {
            do {
                val descripcion = fila.getString(fila.getColumnIndex("descripcion"))
                val indicaciones = fila.getString(fila.getColumnIndex("indicaciones"))

                listaRecetas.add(Receta(descripcion, indicaciones, ""))

            } while (fila.moveToNext())
        }

        var recetasAdapter = RecetasAdapter(fragContext, listaRecetas)
        lvRecetas.adapter = recetasAdapter
    }

    inner class RecetasAdapter : BaseAdapter {

        private var recetasList = ArrayList<Receta>()
        private var context: Context? = null

        constructor(context: Context, recetasList: ArrayList<Receta>) : super() {
            this.recetasList = recetasList
            this.context = context
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
            val view: View?
            val vh: ViewHolder

            if (convertView == null) {
                view = layoutInflater.inflate(R.layout.receta, parent, false)
                vh = ViewHolder(view)
                view.tag = vh
                //Log.i("JSA", "set Tag for ViewHolder, position: " + position)
            } else {
                view = convertView
                vh = view.tag as ViewHolder
            }

            var mReceta = recetasList[position]

            vh.tvDescripcion.text = mReceta.descripcion
            vh.tvIndicaciones.text = mReceta.indicaciones

            return view
        }

        override fun getItem(position: Int): Any {
            return recetasList[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return recetasList.size
        }
    }

    private class ViewHolder(view: View?) {
        val tvDescripcion: TextView
        val tvIndicaciones: TextView
        //val ivEdit: ImageView

        init {
            this.tvDescripcion = view?.findViewById(R.id.tvDescripcion) as TextView
            this.tvIndicaciones = view?.findViewById(R.id.tvIndicaciones) as TextView
            //this.ivEdit = view?.findViewById(R.id.ivEdit) as ImageView
        }
    }
}