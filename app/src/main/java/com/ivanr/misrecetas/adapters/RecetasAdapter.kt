package com.ivanr.misrecetas.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.ivanr.misrecetas.MainActivity
import com.ivanr.misrecetas.R
import com.ivanr.misrecetas.clases.Receta
import com.ivanr.misrecetas.utilidades.AdminSQLite
import com.ivanr.misrecetas.utilidades.Utilidades

class RecetasAdapter : BaseAdapter {
    val util = Utilidades ()
    private var recetasList = ArrayList<Receta>()
    private var context: Context?

    constructor(context: Context, recetasList: ArrayList<Receta>) : super() {
        this.recetasList = recetasList
        this.context = context
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        val view: View?
        val vh: ViewHolder
        var mReceta = recetasList[position]

        if (convertView == null) {
            view = LayoutInflater.from(this.context).inflate(R.layout.receta, parent, false)
            vh = ViewHolder(view, position, mReceta.v_id, mReceta.v_favorito)
            view.tag = vh
        } else {
            view = convertView
            vh = view.tag as ViewHolder
        }

        vh.tvDescripcion.text = mReceta.v_descripcion
        vh.tvIndicaciones.text = mReceta.v_elaboracion
        if (mReceta.v_favorito == "S") {
            vh.btFavorito.setImageResource(R.drawable.ic_corazon_lleno)
        } else {
            vh.btFavorito.setImageResource(R.drawable.ic_corazon_vacio)
        }
        vh.ivImagen.setImageBitmap(mReceta.v_foto)
        asignaAcciones(vh, view, recetasList, mReceta)

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

    fun asignaAcciones (vh: ViewHolder, view: View?, recetasList:ArrayList<Receta>, mReceta: Receta) {
        vh.tvDescripcion.setOnClickListener {
            navegar_detalle(mReceta)
        }
        vh.tvIndicaciones.setOnClickListener {
            navegar_detalle(mReceta)
        }
        vh.btBorrar.setOnClickListener {
            val admin = AdminSQLite(view?.context, "recetas", null, 1)
            admin.borrarReceta(admin, mReceta.v_id)
            recetasList.remove(mReceta)
            notifyDataSetChanged()
        }
        vh.btFavorito.setOnClickListener {
            val admin = AdminSQLite(view?.context, "recetas", null, 1)
            if (mReceta.v_favorito == "N") {
                admin.actualizar( admin, "recetas", "favorito", "S", mReceta.v_id)
                mReceta.v_favorito = "S"
                vh.btFavorito.setImageResource(R.drawable.ic_corazon_lleno)
            } else {
                admin.actualizar(admin, "recetas", "favorito", "N", mReceta.v_id)
                mReceta.v_favorito = "N"
                vh.btFavorito.setImageResource(R.drawable.ic_corazon_vacio)
            }
            notifyDataSetChanged()
        }
    }

    fun navegar_detalle (p_receta: Receta) {
        val intent = Intent(context, MainActivity::class.java)
        val recetas = ArrayList<Receta>()
        recetas.add(p_receta)
        intent.putExtra("receta", recetas)
        AppCompatActivity().startActivity(intent)
    }

    class ViewHolder(view: View?, position: Int, p_id_receta: Int?, p_favorito: String?) {
        val tvDescripcion: TextView
        val tvIndicaciones: TextView
        val btFavorito: ImageButton
        val btBorrar: ImageButton
        val ivImagen: ImageView
        init {
            this.tvDescripcion = view?.findViewById(R.id.tvDescripcion) as TextView
            this.tvIndicaciones = view.findViewById(R.id.tvIndicaciones) as TextView
            this.btFavorito = view.findViewById(R.id.btFavorito)
            this.btBorrar = view.findViewById(R.id.btBorrar)
            this.ivImagen = view.findViewById(R.id.iv_Receta)
        }
    }
}