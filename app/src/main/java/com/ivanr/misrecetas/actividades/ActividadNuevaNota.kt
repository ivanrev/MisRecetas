package com.ivanr.misrecetas.actividades

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ivanr.misrecetas.databinding.ActivityNuevaNotaBinding
import com.ivanr.misrecetas.utilidades.AdminSQLite
import com.ivanr.misrecetas.utilidades.Parametros
import com.ivanr.misrecetas.utilidades.Utilidades

class ActividadNuevaNota: AppCompatActivity() {
    private lateinit var binding: ActivityNuevaNotaBinding
    private val rParam = Parametros
    private val util = Utilidades
    var v_id_receta:Int = -1
    var vg_imagen: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNuevaNotaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var parametros: Bundle? = getIntent()?.getExtras()
        v_id_receta = parametros!!.getInt("id_receta")

        binding.btGuardarNota.setOnClickListener { guardarNota() }
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

        admin.creaNotaReceta(admin, v_id_receta, binding.etNotaFecha.getText().toString(),
                                        binding.etNotaNotas.getText().toString(), vg_imagen)
        util.mensaje_nuevo(binding.root.rootView, "Nota guardada correctamente")
        //Volvemos atras y actualizarmos los datos al crear el MainActivity
        //val intent = Intent(this, MainActivity2::class.java)
        //startActivity(intent)
        onBackPressed()
    }
}