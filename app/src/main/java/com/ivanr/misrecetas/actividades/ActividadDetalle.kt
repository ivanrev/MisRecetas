package com.ivanr.misrecetas.actividades

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.ivanr.misrecetas.R
import com.ivanr.misrecetas.ui.det.SectionsAdapterDet
import com.ivanr.misrecetas.utilidades.AdminSQLite
import com.ivanr.misrecetas.utilidades.Parametros

class ActividadDetalle : AppCompatActivity() {
    val rParam = Parametros

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle)
        val sectionsPagerAdapter = SectionsAdapterDet(this, supportFragmentManager)
        val viewPager: ViewPager = findViewById(R.id.view_pager_det)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs_det)
        tabs.setupWithViewPager(viewPager)
        var parametros: Bundle? = this.getIntent()?.getExtras()
        var v_id_receta = parametros!!.getInt("id_receta")

        findViewById<MaterialButton>(R.id.ibDet_atras).setOnClickListener {
            onBackPressed()
        }
       /*
        findViewById<ImageButton>(R.id.ibDet_compartir).setOnClickListener {  }
        findViewById<ImageButton>(R.id.ibDet_borrar).setOnClickListener {}*/

        val admin = AdminSQLite(this, "recetas", null, rParam.VERSION_BD)
        var fila = admin.consultar(admin, "select codigo, descripcion, elaboracion, ingredientes, favorito, url, "+
                                                        " foto, categoria, maquina_cocinado "+
                                                " from recetas where codigo = " + v_id_receta +
                                                " order by codigo desc")
        var listaRecetas = admin.carga_lista_recetas(fila, 1)
        var vr_receta = listaRecetas.first()

        findViewById<TextView>(R.id.tv_det_titulo).setText(vr_receta.v_descripcion)
        findViewById<ImageView>(R.id.iv_det_imagen).setImageBitmap(vr_receta.get_foto())
    }
}