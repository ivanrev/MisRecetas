package com.ivanr.misrecetas.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.ivanr.misrecetas.R
import com.ivanr.misrecetas.clases.Nota

class NotasAdapter: BaseAdapter {
    private var notasList = ArrayList<Nota>()
    private var context: Context?

    constructor(context: Context?, notasList: ArrayList<Nota>) : super() {
        this.notasList = notasList
        this.context = context
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        val view: View?
        val vh: ViewHolder
        var cl_nota = notasList[position]
        if (convertView == null) {
            view = LayoutInflater.from(this.context).inflate(R.layout.list_notas, parent, false)
            vh = ViewHolder(view)
            view.tag = vh
        } else {
            view = convertView
            vh = view.tag as ViewHolder
        }
        vh.tv_nota_fecha.setText(cl_nota.v_fecha.toString())
        vh.tv_nota_descripcion.setText(cl_nota.v_descripcion)

        return view
    }

    override fun getItem(position: Int): Any {
        return notasList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return notasList.size
    }

    class ViewHolder(view: View?) {
        val tv_nota_fecha: TextView
        val tv_nota_descripcion: TextView
        init {
            this.tv_nota_fecha = view!!.findViewById(R.id.tv_nota_fecha)
            this.tv_nota_descripcion = view.findViewById(R.id.tv_nota_descripcion)
        }
    }
}