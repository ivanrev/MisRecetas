package com.ivanr.misrecetas.actividades

import android.os.Bundle
import android.widget.TextView
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import com.ivanr.misrecetas.R
import com.ivanr.misrecetas.ui.det.SectionsAdapterDet

class ActividadDetalle : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actividad_detalle)

        val sectionsPagerAdapter = SectionsAdapterDet(this, supportFragmentManager)
        val viewPager: ViewPager = findViewById(R.id.view_pager_det)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs_det)
        tabs.setupWithViewPager(viewPager)

        var parametros: Bundle? = this.getIntent()?.getExtras()
        var v_descripcion = parametros!!.getString("descripcion_receta")
        findViewById<TextView>(R.id.tv_det_titulo).setText(v_descripcion)
    }
}