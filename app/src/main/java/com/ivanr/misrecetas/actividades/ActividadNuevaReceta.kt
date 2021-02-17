package com.ivanr.misrecetas.actividades

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.ivanr.misrecetas.MainActivity
import com.ivanr.misrecetas.R
import com.ivanr.misrecetas.utilidades.AdminSQLite
import com.ivanr.misrecetas.utilidades.Utilidades
import java.io.IOException


class ActividadNuevaReceta : AppCompatActivity() {
    val util = Utilidades()
    lateinit var etDescripcion: TextView
    lateinit var etElaboracion:TextView
    lateinit var etUrl:TextView
    private val PHOTO_SELECTED = 0
    var vg_imagen_bitmap: Bitmap = assetsToBitmap("ic_launcher_background")!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.nueva_receta)
        etDescripcion = findViewById(R.id.etDescripcion)
        etElaboracion = findViewById(R.id.etIndicaciones)
        etUrl = findViewById(R.id.etUrl)

        findViewById<Button>(R.id.bt_Crear_Receta).setOnClickListener {
            crearReceta()
        }
        findViewById<ImageButton>(R.id.ib_ir_url).setOnClickListener {
            var v_url = etUrl.getText().toString()
            if (v_url != "" ) {
                val uri: Uri = Uri.parse(v_url)
                val intent = Intent(Intent.ACTION_VIEW, uri)
                startActivity(intent)
            }
            else {
                util.mensaje(this, "No hay URL para navegar")
            }
        }
        findViewById<Button>(R.id.btSeImagen).setOnClickListener {
            seleccionarImagen()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode== RESULT_OK && requestCode == PHOTO_SELECTED) {
            val selectedImage = data!!.data
            findViewById<ImageView>(R.id.ivImagenReceta).setImageURI(selectedImage)
            vg_imagen_bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage)
        }
    }

    private fun assetsToBitmap(fileName:String): Bitmap?{
        return try{
            val stream = assets.open(fileName)
            BitmapFactory.decodeStream(stream)
        }catch (e: IOException){
            e.printStackTrace()
            null
        }
    }

    fun seleccionarImagen() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, PHOTO_SELECTED)
    }

    fun crearReceta () {
        val admin = AdminSQLite(this, "recetas", null, 1)
        admin.creaReceta(this, admin, etDescripcion.getText().toString(), etElaboracion.getText().toString(), etUrl.getText().toString(), vg_imagen_bitmap, "N")
        //Volvemos atras y actualizarmos los datos al crear el MainActivity
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}
