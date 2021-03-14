package com.ivanr.misrecetas.actividades

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.ivanr.misrecetas.R
import com.ivanr.misrecetas.clases.Categoria
import com.ivanr.misrecetas.databinding.ActivityNuevaCategoriaBinding
import com.ivanr.misrecetas.utilidades.AdminSQLite
import com.ivanr.misrecetas.utilidades.Parametros
import com.ivanr.misrecetas.utilidades.Utilidades

class ActividadNuevaCategoria: AppCompatActivity() {
    private lateinit var binding: ActivityNuevaCategoriaBinding
    private val rParam = Parametros
    private val util = Utilidades
    private var vg_imagen: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNuevaCategoriaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btCrearCategoria.setOnClickListener { guardarCategoria() }
        binding.ivNuevaCategoriaImagen.setOnLongClickListener { seleccionarImagen() }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode== RESULT_OK && requestCode == rParam.PHOTO_SELECTED) {
            val selectedImage = data!!.data
            binding.ivNuevaCategoriaImagen.setImageURI(selectedImage)
            vg_imagen = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage)
        }
    }

    fun seleccionarImagen(): Boolean {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, rParam.PHOTO_SELECTED)
        return true
    }

    fun guardarCategoria () {
        val admin = AdminSQLite(this, "recetas", null, rParam.VERSION_BD)
        var categoria = Categoria (null, null, binding.etNuevaCategoriaDesc.getText().toString(), vg_imagen)
        admin.creaCategoria(admin, categoria)
        util.mensaje_nuevo(binding.root.rootView, "Categoria guardada correctamente")
        onBackPressed()
    }
}