package com.ivanr.misrecetas.adapters

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ivanr.misrecetas.R
import com.ivanr.misrecetas.actividades.ActividadDetalle
import com.ivanr.misrecetas.clases.Receta
import com.ivanr.misrecetas.databinding.ListRecetasBinding
import com.ivanr.misrecetas.listener.OnClick_ListReceta_Listener
import com.ivanr.misrecetas.utilidades.AdminSQLite
import com.ivanr.misrecetas.utilidades.Parametros
import com.ivanr.misrecetas.utilidades.Utilidades


class RecetasAdapter (private val recetasList: ArrayList<Receta>, private val listener: OnClick_ListReceta_Listener) : RecyclerView.Adapter<RecetasAdapter.ViewHolder>() {
    private val util = Utilidades
    private val rParam = Parametros
    lateinit var vista_actual: View

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecetasAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_recetas, parent, false)
        vista_actual = view
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
            bindHolder.tvDescripcion.setOnClickListener {
                navegar_detalle(receta)
                //listener.onClick_listRecetas(receta, position, "navegar")
                }
            bindHolder.tvIndicaciones.setOnClickListener {
                navegar_detalle(receta)
                //listener.onClick_listRecetas(receta, position, "navegar")
            }
            bindHolder.btFavorito.setOnClickListener {
                actualiza_favorito(receta)
                //listener.onClick_listRecetas(receta, position, "favorito")
            }
            bindHolder.root.setOnClickListener {
                navegar_detalle(receta)
                //listener.onClick_listRecetas(receta, position, "navegar")
            }
        }
    }

    fun actualiza_favorito (receta:Receta) {
        val admin = AdminSQLite(vista_actual.context, "recetas", null, rParam.VERSION_BD)
        val v_campos = arrayOfNulls<String>(5)
        var v_valores = arrayOfNulls<String>(5)
        v_campos[0] = "favorito"

        if (receta.v_favorito == "N") {
            v_valores[0] = "S"
            admin.actualizar(admin, "recetas", receta.v_id,v_campos, v_valores)
            receta.v_favorito = "S"
            //vh.btFavorito.setImageResource(R.drawable.ic_corazon_lleno)
        } else {
            v_valores[0] = "N"
            admin.actualizar(admin, "recetas", receta.v_id, v_campos, v_valores)
            receta.v_favorito = "N"
            //vh.btFavorito.setImageResource(R.drawable.ic_corazon_vacio)
        }
        notifyDataSetChanged()
    }
    fun navegar_detalle(p_receta: Receta) {
        val intent = Intent(vista_actual.context, ActividadDetalle::class.java)
        val bundle = Bundle()
        bundle.putInt("id_receta", p_receta.v_id)
        bundle.putString("descripcion_receta", p_receta.v_descripcion)
        bundle.putString("favorito_receta", p_receta.v_favorito)
        intent.putExtras(bundle)
        vista_actual.context.startActivity(intent)
    }
}