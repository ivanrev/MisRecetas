package com.ivanr.misrecetas.actividades

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.ivanr.misrecetas.R
import com.ivanr.misrecetas.clases.Receta
import com.ivanr.misrecetas.databinding.ActivityDetalleBinding
import com.ivanr.misrecetas.databinding.ActivityNuevaNotaBinding
import com.ivanr.misrecetas.ui.det.SectionsAdapterDet
import com.ivanr.misrecetas.utilidades.AdminSQLite
import com.ivanr.misrecetas.utilidades.Parametros
import com.ivanr.misrecetas.utilidades.Utilidades
import java.io.ByteArrayOutputStream

class ActividadDetalle : AppCompatActivity() {
    private val rParam = Parametros
    private val util = Utilidades
    private lateinit var binding: ActivityDetalleBinding
    private lateinit var vr_receta: Receta

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetalleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val sectionsPagerAdapter = SectionsAdapterDet(this, supportFragmentManager)
        binding.viewPagerDet.adapter = sectionsPagerAdapter
        binding.tabsDet.setupWithViewPager(binding.viewPagerDet)
        cargar_parametros()
        binding.ibDetAtras.setOnClickListener { onBackPressed() }
        binding.ibDetBorrar.setOnClickListener {
            val admin = AdminSQLite(this, "recetas", null, rParam.VERSION_BD)
            admin.borrarReceta(admin, vr_receta.v_id)
            onBackPressed()
        }
        binding.ibDetCompartir.setOnClickListener {compartir()}
        binding.tvDetTitulo.setText(vr_receta.v_descripcion)
        binding.ivDetImagen.setImageBitmap(vr_receta.get_foto())
    }

    fun cargar_parametros () {
        var parametros: Bundle? = this.getIntent()?.getExtras()
        var v_id_receta = parametros!!.getInt("id_receta")

        val admin = AdminSQLite(this, "recetas", null, rParam.VERSION_BD)
        var fila = admin.consultar(admin, "select codigo, descripcion, elaboracion, ingredientes, favorito, url, "+
                " foto, categoria, maquina_cocinado "+
                " from recetas where codigo = " + v_id_receta +
                " order by codigo desc")
        var listaRecetas = admin.carga_lista_recetas(fila, 1)
        vr_receta = listaRecetas.first()

    }
    fun compartir () {
        val intent_compartir = Intent(Intent.ACTION_SEND)
        intent_compartir.type = "image/*"
        val valores = ContentValues()
        valores.put(MediaStore.Images.Media.TITLE, binding.tvDetTitulo.text.toString())
        valores.put(MediaStore.Images.Media.MIME_TYPE, "image/*")
        val v_imagen = util.getImageUriFromBitmap (this, vr_receta.get_foto()!!)
        intent_compartir.putExtra(Intent.EXTRA_STREAM, v_imagen)
        startActivity(Intent.createChooser(intent_compartir, binding.tvDetTitulo.text.toString()))

    }
}