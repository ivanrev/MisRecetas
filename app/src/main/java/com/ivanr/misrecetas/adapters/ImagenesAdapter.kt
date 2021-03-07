package com.ivanr.misrecetas.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ivanr.misrecetas.R
import com.ivanr.misrecetas.clases.ImagenesReceta
import com.ivanr.misrecetas.databinding.ListImagenesBinding
import com.ivanr.misrecetas.listener.OnClick_DetImagen_Listener

class ImagenesAdapter(private val values: ArrayList<ImagenesReceta>, private val detImagenListener: OnClick_DetImagen_Listener)
    : RecyclerView.Adapter<ImagenesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_imagenes, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val imagen = values[position]
        with(holder) {
            setListener(imagen, position)
            if (imagen.v_foto != null) {
                holder.bindHolder.ivListaImagen.setImageBitmap(imagen.v_foto)
            }
            else {
                holder.bindHolder.ivListaImagen.setImageResource(R.drawable.ic_imagen_vacio)
            }
        }
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val bindHolder = ListImagenesBinding.bind (view)

        fun setListener (imagen: ImagenesReceta, position: Int) {
            bindHolder.ivListaImagen.setOnClickListener {detImagenListener.onClick_detImagen(imagen, position)}
        }
    }
}
