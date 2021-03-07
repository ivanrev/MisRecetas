package com.ivanr.misrecetas.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.ivanr.misrecetas.R
import com.ivanr.misrecetas.actividades.ActividadDetalle
import com.ivanr.misrecetas.actividades.ActividadNuevaNota
import com.ivanr.misrecetas.adapters.ImagenesAdapter
import com.ivanr.misrecetas.adapters.NotasAdapter
import com.ivanr.misrecetas.clases.Nota
import com.ivanr.misrecetas.ui.det_Imagenes.ImagenesViewModel
import com.ivanr.misrecetas.ui.det_Notas.NotasViewModel
import com.ivanr.misrecetas.utilidades.AdminSQLite
import com.ivanr.misrecetas.utilidades.Parametros
import com.ivanr.misrecetas.utilidades.Utilidades

class FragmentDetNotas : Fragment() {

    private lateinit var notasViewModel: NotasViewModel
    val rParam = Parametros
    val util = Utilidades
    var v_id_receta:Int = -1
    lateinit var admin: AdminSQLite
    lateinit var lvNotas: ListView
    lateinit var notasAdapter: NotasAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        notasViewModel = ViewModelProvider(this).get(NotasViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_det_notas, container, false)
        lvNotas = root.findViewById(R.id.lvNotas)
        var parametros: Bundle? = getActivity()?.getIntent()?.getExtras()
        v_id_receta = parametros!!.getInt("id_receta")
        val bt_boton = root.findViewById<FloatingActionButton>(R.id.bt_detnot_anadir).setOnClickListener {
            nuevaNota()
        }
        return root
    }
    override fun onResume() {
        super.onResume()
        admin = AdminSQLite(activity, "recetas", null, rParam.VERSION_BD)
        consultarNotas(v_id_receta)
    }
    companion object {
        fun newInstance() = FragmentDetNotas().apply {}
    }

    fun consultarNotas (p_id_receta:Int) {
        var fila = admin.consultar(admin,"select id_nota, receta, fecha, notas, foto from recetas_his where receta = " +
                                                    p_id_receta + " order by id_nota asc")
        var v_ar_notas = admin.carga_lista_notas(fila)
        notasAdapter = NotasAdapter(activity, v_ar_notas)
        lvNotas.adapter = notasAdapter
    }
    fun nuevaNota () {
        val intent = Intent(context, ActividadNuevaNota::class.java)
        val bundle = Bundle()
        bundle.putInt("id_receta", v_id_receta)
        intent.putExtras(bundle)
        startActivity(intent)
    }
}