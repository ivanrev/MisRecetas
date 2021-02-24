package com.ivanr.misrecetas.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import com.ivanr.misrecetas.R
import com.ivanr.misrecetas.clases.ImagenesReceta

class ImagenesAdapter:BaseAdapter {
    private var imagenesList = ArrayList<ImagenesReceta>()
    private var context: Context?

    constructor(context: Context?, recetasList: ArrayList<ImagenesReceta>) : super() {
        this.imagenesList = recetasList
        this.context = context
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        val view: View?
        val vh: ViewHolder
        var cl_imagen = imagenesList[position]
        if (convertView == null) {
            view = LayoutInflater.from(this.context).inflate(R.layout.imagenes, parent, false)
            vh = ViewHolder(view)
            view.tag = vh
        } else {
            view = convertView
            vh = view.tag as ViewHolder
        }
        vh.ivImagen.setImageBitmap(cl_imagen.v_foto)
        asignaAcciones(vh, view, imagenesList, cl_imagen)

        return view
    }
    override fun getItem(position: Int): Any {
        return imagenesList[position]
    }
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
    override fun getCount(): Int {
        return imagenesList.size
    }

    fun asignaAcciones(vh: ViewHolder, view: View?, recetasList: ArrayList<ImagenesReceta>, mReceta: ImagenesReceta) {
    }

    class ViewHolder(view: View?) {
        val ivImagen: ImageView
        init {
            this.ivImagen = view!!.findViewById(R.id.iv_lista_imagen)
        }
    }
}