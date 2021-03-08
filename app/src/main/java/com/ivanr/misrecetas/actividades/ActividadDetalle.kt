package com.ivanr.misrecetas.actividades

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.itextpdf.text.Document
import com.itextpdf.text.Paragraph
import com.itextpdf.text.pdf.PdfWriter
import com.ivanr.misrecetas.BuildConfig
import com.ivanr.misrecetas.clases.Receta
import com.ivanr.misrecetas.databinding.ActivityDetalleBinding
import com.ivanr.misrecetas.ui.det.SectionsAdapterDet
import com.ivanr.misrecetas.utilidades.AdminSQLite
import com.ivanr.misrecetas.utilidades.Parametros
import com.ivanr.misrecetas.utilidades.Utilidades
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*


class ActividadDetalle : AppCompatActivity() {
    private val STORAGE_CODE: Int = 100;
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
        binding.ibDetCompartir.setOnClickListener {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M){
                //system OS >= Marshmallow(6.0), check permission is enabled or not
                if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                    //permission was not granted, request it
                    val permissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    requestPermissions(permissions, STORAGE_CODE)
                }
                else{
                    //permission already granted, call savePdf() method
                    compartir_pdf()
                }
            }
            else{
                //system OS < marshmallow, call savePdf() method
                compartir_pdf()
            }
        }
        binding.tvDetTitulo.setText(vr_receta.v_descripcion)
        binding.ivDetImagen.setImageBitmap(vr_receta.get_foto())
    }

    fun cargar_parametros () {
        var parametros: Bundle? = this.getIntent()?.getExtras()
        var v_id_receta = parametros!!.getInt("id_receta")

        val admin = AdminSQLite(this, "recetas", null, rParam.VERSION_BD)
        var fila = admin.consultar(
            admin, "select codigo, descripcion, elaboracion, ingredientes, favorito, url, " +
                    " foto, categoria, maquina_cocinado " +
                    " from recetas where codigo = " + v_id_receta +
                    " order by codigo desc"
        )
        var listaRecetas = admin.carga_lista_recetas(fila, 1)
        vr_receta = listaRecetas.first()

    }
    fun compartir_pdf () {
        val doc_pdf = vr_receta.receta_a_pdf(this, vr_receta)
        var file = File(doc_pdf)
        val contentUri = FileProvider.getUriForFile(this, "com.ivanr.misrecetas.fileprovider", file)
        val intent_compartir = Intent(Intent.ACTION_SEND)
        intent_compartir.type = "application/pdf"
        intent_compartir.putExtra(Intent.EXTRA_STREAM, contentUri)
        startActivity(Intent.createChooser(intent_compartir, "Compartir"));
    }

    fun compartir_img () {
        val intent_compartir = Intent(Intent.ACTION_SEND)
        intent_compartir.type = "image/*"
        val valores = ContentValues()
        valores.put(MediaStore.Images.Media.TITLE, binding.tvDetTitulo.text.toString())
        valores.put(MediaStore.Images.Media.MIME_TYPE, "image/*")
        val v_imagen = util.getImageUriFromBitmap(this, vr_receta.get_foto()!!)
        intent_compartir.putExtra(Intent.EXTRA_STREAM, v_imagen)
        startActivity(Intent.createChooser(intent_compartir, binding.tvDetTitulo.text.toString()))
    }

}