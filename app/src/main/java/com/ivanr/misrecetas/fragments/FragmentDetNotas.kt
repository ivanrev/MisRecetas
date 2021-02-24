package com.ivanr.misrecetas.fragments

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
import com.ivanr.misrecetas.adapters.ImagenesAdapter
import com.ivanr.misrecetas.adapters.NotasAdapter
import com.ivanr.misrecetas.clases.Nota
import com.ivanr.misrecetas.ui.det_Imagenes.ImagenesViewModel
import com.ivanr.misrecetas.ui.det_Notas.NotasViewModel
import com.ivanr.misrecetas.utilidades.AdminSQLite
import com.ivanr.misrecetas.utilidades.Parametros

class FragmentDetNotas : Fragment() {

    private lateinit var notasViewModel: NotasViewModel
    val rParam = Parametros
    var v_id_receta:Int = -1
    lateinit var admin: AdminSQLite
    lateinit var lvNotas: ListView
    lateinit var notasAdapter: NotasAdapter
    lateinit var v_ar_notas:ArrayList<Nota>


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        notasViewModel = ViewModelProvider(this).get(NotasViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_det_notas, container, false)
        lvNotas = root.findViewById(R.id.lvNotas)

        var parametros: Bundle? = getActivity()?.getIntent()?.getExtras()
        v_id_receta = parametros!!.getInt("id_receta")
        admin = AdminSQLite(activity, "recetas", null, rParam.VERSION_BD)
        consultarNotas(v_id_receta)
        return root
    }
    companion object {
        fun newInstance() = FragmentDetNotas().apply {}
    }

    fun consultarNotas (p_id_receta:Int) {
        var fila = admin.consultar(admin,"select receta, linea, fecha, notas from recetas_his where receta = " + v_id_receta + " order by linea asc")
        var v_ar_notas = admin.carga_lista_notas(fila)
        notasAdapter = NotasAdapter(activity, v_ar_notas)
        lvNotas.adapter = notasAdapter

    }
}