package com.ivanr.misrecetas.actividades

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ivanr.misrecetas.databinding.ActivityNuevaNotaBinding
import com.ivanr.misrecetas.utilidades.AdminSQLite
import com.ivanr.misrecetas.utilidades.Parametros
import com.ivanr.misrecetas.utilidades.Utilidades
import java.util.*


class ActividadNuevaNota: AppCompatActivity() {
    private lateinit var binding: ActivityNuevaNotaBinding
    private val rParam = Parametros
    private val util = Utilidades
    var v_id_receta:Int = -1
    var vg_imagen: Bitmap? = null
    lateinit var v_fecha:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNuevaNotaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var parametros: Bundle? = getIntent()?.getExtras()
        v_id_receta = parametros!!.getInt("id_receta")
        binding.btGuardarNota.setOnClickListener { guardarNota() }
        val v_hoy = Calendar.getInstance()
        binding.dpNotaFecha.init(v_hoy.get(Calendar.YEAR), v_hoy.get(Calendar.MONTH), v_hoy.get(Calendar.DAY_OF_MONTH)) { view, year, month, day ->
            val month = month + 1
            v_fecha = " $day/$month/$year"
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode== RESULT_OK && requestCode == rParam.PHOTO_SELECTED) {
            val selectedImage = data!!.data
            //findViewById<ImageView>(R.id.ivImagenReceta).setImageURI(selectedImage)
            //vg_imagen = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage)
        }
    }

    fun seleccionarImagen() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, rParam.PHOTO_SELECTED)
    }
    fun guardarNota () {
        val admin = AdminSQLite(this, "recetas", null, rParam.VERSION_BD)
        admin.creaNotaReceta(admin, v_id_receta, v_fecha, binding.etNotaNotas.getText().toString(), vg_imagen)
        util.mensaje_nuevo(binding.root.rootView, "Nota guardada correctamente")
        onBackPressed()
    }
}