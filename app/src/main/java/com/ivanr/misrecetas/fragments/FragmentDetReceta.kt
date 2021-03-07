package com.ivanr.misrecetas.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.ivanr.misrecetas.R
import com.ivanr.misrecetas.clases.Receta
import com.ivanr.misrecetas.ui.det_Receta.RecetaViewModel
import com.ivanr.misrecetas.utilidades.AdminSQLite
import com.ivanr.misrecetas.utilidades.Parametros
import com.ivanr.misrecetas.utilidades.Utilidades


class FragmentDetReceta : Fragment(), View.OnClickListener {
    private val rParam = Parametros
    private val util = Utilidades
    private lateinit var vr_receta: Receta
    private lateinit var root: View
    private lateinit var recetaViewModel: RecetaViewModel
    private lateinit var iv_Favorito: ImageView
    private lateinit var et_Elaboracion: EditText
    private lateinit var et_Ingredientes: EditText
    private lateinit var et_Url: EditText
    private lateinit var bt_boton:FloatingActionButton

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        recetaViewModel = ViewModelProvider(this).get(RecetaViewModel::class.java)
        root = inflater.inflate(R.layout.fragment_det_receta, container, false)

        asocia_controles()
        recupera_parametros()
        inicializa_datos ()
        bt_boton.setOnClickListener {
            guardarCambios(vr_receta.v_id, vr_receta.v_descripcion!!, vr_receta.v_favorito!!)
        }
        return root
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FragmentUltimas.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            FragmentDetReceta().apply {
            }
    }

    fun asocia_controles () {
        iv_Favorito = root.findViewById(R.id.ivDet_Favorito)
        et_Elaboracion = root.findViewById(R.id.etDet_elaboracion)
        et_Ingredientes = root.findViewById(R.id.etDet_ingredientes)
        et_Url = root.findViewById(R.id.etDet_Url)
        et_Url.setOnClickListener(this)
        bt_boton = root.findViewById(R.id.bt_det_guardar)
    }

    fun recupera_parametros () {
        var parametros: Bundle? = getActivity()?.getIntent()?.getExtras()
        val v_id_receta = parametros!!.getInt("id_receta")
        val admin = AdminSQLite(activity, "recetas", null, rParam.VERSION_BD)
        var fila = admin.consultar(
            admin,
            "select codigo, descripcion, elaboracion, ingredientes, favorito, url, foto, categoria, maquina_cocinado "+
                    " from recetas where codigo = " + v_id_receta + " order by codigo desc"
        )
        var listaRecetas = admin.carga_lista_recetas(fila, 1)
        vr_receta = listaRecetas.first()
    }
    fun inicializa_datos () {
        if (vr_receta.v_favorito=="S") {
            iv_Favorito.setImageResource(R.drawable.ic_corazon_lleno)
        } else {
            iv_Favorito.setImageResource(R.drawable.ic_corazon_vacio)
        }
        et_Elaboracion.setText(vr_receta.v_elaboracion)
        et_Ingredientes.setText(vr_receta.v_ingredientes)
        et_Url.setText(vr_receta.v_url)

    }

    override fun onClick(p0: View?) {
        if (p0!!.id == R.id.etDet_Url) {
            var v_url = et_Url.text.toString()
            if (v_url != "" ) {
                util.navega_url(requireActivity(), v_url)
            } else {}
        }
    }

    fun guardarCambios (p_id_receta:Int, p_descripcion:String, p_favorito:String) {
        var v_descripcion = p_descripcion
        var v_favorito = p_favorito
        var v_ingredientes = root.findViewById<EditText>(R.id.etDet_ingredientes).text.toString()
        var v_elaboracion = root.findViewById<EditText>(R.id.etDet_elaboracion).text.toString()
        var v_url = root.findViewById<EditText>(R.id.etDet_Url).text.toString()

        val v_campos = arrayOfNulls<String>(5)
        var v_valores = arrayOfNulls<String>(5)
        v_campos[0] = "descripcion"
        v_campos[1] = "elaboracion"
        v_campos[2] = "ingredientes"
        v_campos[3] = "url"
        v_campos[4] = "favorito"
        v_valores[0] = v_descripcion
        v_valores[1] = v_elaboracion
        v_valores[2] = v_ingredientes
        v_valores[3] = v_url
        v_valores[4] = v_favorito

        val admin = AdminSQLite(activity, "recetas", null, rParam.VERSION_BD)
        admin.actualizar(admin, "recetas", p_id_receta, v_campos, v_valores)
    }

}