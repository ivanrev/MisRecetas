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
import com.ivanr.misrecetas.databinding.ActivityDetalleBinding
import com.ivanr.misrecetas.databinding.FragmentDetRecetaBinding
import com.ivanr.misrecetas.ui.det_Receta.RecetaViewModel
import com.ivanr.misrecetas.utilidades.AdminSQLite
import com.ivanr.misrecetas.utilidades.Parametros
import com.ivanr.misrecetas.utilidades.Utilidades


class FragmentDetReceta : Fragment(), View.OnClickListener {
    private lateinit var binding: FragmentDetRecetaBinding
    private val rParam = Parametros
    private val util = Utilidades
    private lateinit var vr_receta: Receta
    private lateinit var root: View
    private lateinit var recetaViewModel: RecetaViewModel

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        recetaViewModel = ViewModelProvider(this).get(RecetaViewModel::class.java)
        //root = inflater.inflate(R.layout.fragment_det_receta, container, false)
        binding = FragmentDetRecetaBinding.inflate(inflater, container, false)
        //setContentView(binding.root)

        recupera_parametros()
        inicializa_datos ()
        binding.etDetUrl.setOnClickListener(this)
        binding.btDetGuardar.setOnClickListener {
            guardarCambios(vr_receta.v_id, vr_receta.v_favorito!!)
        }
        return binding.root
    }
    override fun onClick(p0: View?) {
        if (p0!!.id == R.id.etDet_Url) {
            var v_url = binding.etDetUrl.text.toString()
            if (v_url != "" ) {
                util.navega_url(requireActivity(), v_url)
            } else {}
        }
    }
    companion object {
        @JvmStatic
        fun newInstance() =
            FragmentDetReceta().apply {
            }
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
            binding.ivDetFavorito.setImageResource(R.drawable.ic_corazon_lleno)
        } else {
            binding.ivDetFavorito.setImageResource(R.drawable.ic_corazon_vacio)
        }
        binding.etDetDescripcion.setText(vr_receta.v_descripcion)
        binding.etDetElaboracion.setText(vr_receta.v_elaboracion)
        binding.etDetIngredientes.setText(vr_receta.v_ingredientes)
        binding.etDetUrl.setText(vr_receta.v_url)
    }
    fun guardarCambios (p_id_receta:Int, p_favorito:String) {
        val v_campos = arrayOfNulls<String>(5)
        var v_valores = arrayOfNulls<String>(5)
        v_campos[0] = "descripcion"
        v_campos[1] = "elaboracion"
        v_campos[2] = "ingredientes"
        v_campos[3] = "url"
        v_campos[4] = "favorito"
        v_valores[0] = binding.etDetDescripcion.text.toString()
        v_valores[1] = binding.etDetElaboracion.text.toString()
        v_valores[2] = binding.etDetIngredientes.text.toString()
        v_valores[3] = binding.etDetUrl.text.toString()
        v_valores[4] = p_favorito

        val admin = AdminSQLite(activity, "recetas", null, rParam.VERSION_BD)
        admin.actualizar(admin, "recetas", p_id_receta, v_campos, v_valores)
        requireActivity().onBackPressed()
    }

}