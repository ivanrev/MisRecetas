package com.ivanr.misrecetas.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.ivanr.misrecetas.R
import com.ivanr.misrecetas.clases.Categoria

class CategoriasAdapter: BaseAdapter {
    private var categoriasList = ArrayList<Categoria>()
    private var context: Context?

    constructor(context: Context?, notasList: ArrayList<Categoria>) : super() {
        this.categoriasList = notasList
        this.context = context
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        val view: View?
        val vh: ViewHolder
        var cl_nota = categoriasList[position]
        if (convertView == null) {
            view = LayoutInflater.from(this.context).inflate(R.layout.list_notas, parent, false)
            vh = ViewHolder(view)
            view.tag = vh
        } else {
            view = convertView
            vh = view.tag as ViewHolder
        }
        vh.tv_categoria_descripcion.setText(cl_nota.descripcion)

        return view
    }

    override fun getItem(position: Int): Any {
        return categoriasList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return categoriasList.size
    }

    class ViewHolder(view: View?) {
        val tv_categoria_descripcion: TextView
        init {
            this.tv_categoria_descripcion = view!!.findViewById(R.id.tv_categoria_descripcion)
        }
    }
}