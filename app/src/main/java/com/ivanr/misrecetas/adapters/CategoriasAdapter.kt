package com.ivanr.misrecetas.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ivanr.misrecetas.R
import com.ivanr.misrecetas.clases.Categoria

class CategoriasAdapter(private val values: ArrayList<Categoria>) : RecyclerView.Adapter<CategoriasAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_categorias, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.tv_categoria_descripcion.text = item.descripcion
        if (item.foto != null) {
            holder.iv_categoria_foto.setImageBitmap(item.foto)
        }else {holder.iv_categoria_foto.setImageResource(R.drawable.ic_imagen_vacio)}
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tv_categoria_descripcion: TextView = view?.findViewById(R.id.tv_categoria_descripcion)
        val iv_categoria_foto: ImageView = view.findViewById(R.id.iv_categoria_foto)

        override fun toString(): String {
            return super.toString() + " '" + tv_categoria_descripcion.text + "'"
        }
    }
}