package com.ivanr.misrecetas.actividades

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import com.ivanr.misrecetas.R
import com.ivanr.misrecetas.ui.det.SectionsAdapterDet
import com.ivanr.misrecetas.utilidades.AdminSQLite
import com.ivanr.misrecetas.utilidades.Parametros

class ActividadDetalle : AppCompatActivity() {
    val rParam = Parametros

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actividad_detalle)

        val sectionsPagerAdapter = SectionsAdapterDet(this, supportFragmentManager)
        val viewPager: ViewPager = findViewById(R.id.view_pager_det)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs_det)
        tabs.setupWithViewPager(viewPager)

        var parametros: Bundle? = this.getIntent()?.getExtras()
        var v_id_receta = parametros!!.getInt("id_receta")
        var v_descripcion = parametros!!.getString("descripcion_receta")
        var v_favorito = parametros!!.getString("favorito_receta")

        findViewById<TextView>(R.id.tv_det_titulo).setText(v_descripcion)
        findViewById<ImageButton>(R.id.ibDet_atras).setOnClickListener {
            onBackPressed()
        }
        findViewById<ImageButton>(R.id.ibDet_guardar).setOnClickListener {
            var v_descripcion = findViewById<TextView>(R.id.tvDet_DescripcionReceta).text.toString()
            var v_ingredientes = findViewById<EditText>(R.id.etDet_ingredientes).text.toString()
            var v_elaboracion = findViewById<EditText>(R.id.etDet_elaboracion).text.toString()
            var v_url = findViewById<EditText>(R.id.etDet_Url).text.toString()

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
            v_valores[4] = v_favorito!!

            val admin = AdminSQLite(this, "recetas", null, rParam.VERSION_BD)
            admin.actualizar(admin, "recetas", v_id_receta, v_campos, v_valores)
        }
        findViewById<ImageButton>(R.id.ibDet_compartir).setOnClickListener {

        }
        findViewById<ImageButton>(R.id.ibDet_borrar).setOnClickListener {

        }
    }
}