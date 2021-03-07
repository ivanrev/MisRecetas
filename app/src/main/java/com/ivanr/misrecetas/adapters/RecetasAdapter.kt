package com.ivanr.misrecetas.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ivanr.misrecetas.R
import com.ivanr.misrecetas.clases.Receta
import com.ivanr.misrecetas.databinding.ListRecetasBinding
import com.ivanr.misrecetas.listener.OnClick_ListReceta_Listener
import com.ivanr.misrecetas.utilidades.Parametros
import com.ivanr.misrecetas.utilidades.Utilidades


class RecetasAdapter (private val recetasList: ArrayList<Receta>, private val listener: OnClick_ListReceta_Listener) : RecyclerView.Adapter<RecetasAdapter.ViewHolder>() {
    private val util = Utilidades
    private val rParam = Parametros

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecetasAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_recetas, parent, false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: RecetasAdapter.ViewHolder, position: Int) {
        val cl_receta = recetasList[position]
        with(holder) {
            holder.bindHolder.tvDescripcion.text = cl_receta.v_descripcion
            holder.bindHolder.tvIndicaciones.text = cl_receta.v_elaboracion
            if (cl_receta.v_favorito == "S") {
                holder.bindHolder.btFavorito.setImageResource(R.drawable.ic_corazon_lleno)
            } else {
                holder.bindHolder.btFavorito.setImageResource(R.drawable.ic_corazon_vacio)
            }
            if (cl_receta.v_foto != null) {
                holder.bindHolder.ivReceta.setImageBitmap(cl_receta.v_foto)
            }
            else {
                holder.bindHolder.ivReceta.setImageResource(R.drawable.ic_imagen_vacio)
            }

            setListener(cl_receta, position)
        }
    }

    override fun getItemCount(): Int = recetasList.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val bindHolder = ListRecetasBinding.bind (view)

        fun setListener (receta: Receta, position: Int) {
            bindHolder.ivReceta.setOnClickListener {listener.onClick_listRecetas(receta, position, "ampliar_imagem")}
            bindHolder.tvDescripcion.setOnClickListener {listener.onClick_listRecetas(receta, position, "navegar")}
            bindHolder.tvIndicaciones.setOnClickListener {listener.onClick_listRecetas(receta, position, "navegar")}
            bindHolder.btFavorito.setOnClickListener {listener.onClick_listRecetas(receta, position, "favorito")}
            bindHolder.root.setOnClickListener {
                listener.onClick_listRecetas(receta, position, "navegar") }
        }
    }
/*
    fun asignaAcciones(vh: ViewHolder, view: View?, recetasList: ArrayList<Receta>, mReceta: Receta) {
        vh.tvDescripcion.setOnClickListener {
            navegar_detalle(view?.context!!, mReceta)
        }
        vh.tvIndicaciones.setOnClickListener {
            navegar_detalle(view?.context!!, mReceta)
        }
        vh.btBorrar.setOnClickListener {
            val admin = AdminSQLite(view?.context, "recetas", null, rParam.VERSION_BD)
            admin.borrarReceta(admin, mReceta.v_id)
            recetasList.remove(mReceta)
            notifyDataSetChanged()
        }
        vh.btFavorito.setOnClickListener {
            val admin = AdminSQLite(view?.context, "recetas", null, rParam.VERSION_BD)
            val v_campos = arrayOfNulls<String>(5)
            var v_valores = arrayOfNulls<String>(5)
            v_campos[0] = "favorito"

            if (mReceta.v_favorito == "N") {
                v_valores[0] = "S"
                admin.actualizar(admin, "recetas", mReceta.v_id,v_campos, v_valores)
                mReceta.v_favorito = "S"
                vh.btFavorito.setImageResource(R.drawable.ic_corazon_lleno)
            } else {
                v_valores[0] = "N"
                admin.actualizar(admin, "recetas", mReceta.v_id, v_campos, v_valores)
                mReceta.v_favorito = "N"
                vh.btFavorito.setImageResource(R.drawable.ic_corazon_vacio)
            }
            notifyDataSetChanged()
        }
    }
*/
}