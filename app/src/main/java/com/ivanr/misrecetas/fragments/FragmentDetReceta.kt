package com.ivanr.misrecetas.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
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
    private lateinit var iv_imagen: ImageView
    private lateinit var iv_Favorito: ImageView
    private lateinit var tv_descripcion: TextView
    private lateinit var et_Elaboracion: EditText
    private lateinit var et_Ingredientes: EditText
    private lateinit var et_Url: EditText

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        recetaViewModel = ViewModelProvider(this).get(RecetaViewModel::class.java)
        root = inflater.inflate(R.layout.fragment_det_receta, container, false)

        asocia_controles()
        recupera_parametros()
        inicializa_datos ()
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
        iv_imagen = root.findViewById(R.id.ivDet_ImagenReceta)
        iv_Favorito = root.findViewById(R.id.ivDet_Favorito)
        tv_descripcion = root.findViewById(R.id.tvDet_DescripcionReceta)
        et_Elaboracion = root.findViewById(R.id.etDet_elaboracion)
        et_Ingredientes = root.findViewById(R.id.etDet_ingredientes)
        et_Url = root.findViewById(R.id.etDet_Url)

        et_Url.setOnClickListener(this)
    }

    fun recupera_parametros () {
        var parametros: Bundle? = getActivity()?.getIntent()?.getExtras()
        val v_id_receta = parametros!!.getInt("id_receta")
        val admin = AdminSQLite(activity, "recetas", null, rParam.VERSION_BD)
        var fila = admin.consultar(
            admin,
            "select codigo, descripcion, elaboracion, ingredientes, favorito, url, foto from recetas where codigo = " + v_id_receta + " order by codigo desc"
        )
        var listaRecetas = admin.carga_lista_recetas(fila, 1)
        vr_receta = listaRecetas.first()
    }
    fun inicializa_datos () {
        iv_imagen.setImageBitmap(vr_receta.get_foto())
        if (vr_receta.v_favorito=="S") {
            iv_Favorito.setImageResource(R.drawable.ic_corazon_lleno)
        } else {
            iv_Favorito.setImageResource(R.drawable.ic_corazon_vacio)
        }
        tv_descripcion.setText(vr_receta.v_descripcion)
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

}