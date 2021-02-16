package com.ivanr.misrecetas.actividades

import android.app.PendingIntent.getActivity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.ivanr.misrecetas.MainActivity
import com.ivanr.misrecetas.R
import com.ivanr.misrecetas.utilidades.AdminSQLite
import com.ivanr.misrecetas.utilidades.Util


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class ActividadNuevaReceta : AppCompatActivity() {
    val util = Util()
    lateinit var etDescripcion: TextView
    lateinit var etElaboracion:TextView
    lateinit var etUrl:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.nueva_receta)
        asociarControles()
    }
/*
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PHOTO_SELECTED) {
            if (resultCode == getActivity().RESULT_OK) {
                val selectedImage = data.data
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage)
            }
        }
    }*/
    fun asociarControles () {
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
        findViewById<Button>(R.id.btSelImagen).setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            //startActivityForResult(intent, PHOTO_SELECTED)
        }

    }
    fun seleccionarImagen() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        if (intent.resolveActivity(packageManager) != null) {
            startActivityForResult(intent, REQUEST_SELECT_IMAGE_IN_ALBUM)
        }
    }
    fun hacerFoto() {
        val intent1 = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent1.resolveActivity(packageManager) != null) {
            startActivityForResult(intent1, REQUEST_TAKE_PHOTO) }
    }
    companion object {
        private val REQUEST_TAKE_PHOTO = 0
        private val REQUEST_SELECT_IMAGE_IN_ALBUM = 1
    }
    
    fun crearReceta () {
        val admin = AdminSQLite(this, "recetas", null, 1)
        val v_foto = byteArrayOf(0xA1.toByte())
        admin.creaReceta(this, admin, etDescripcion.getText().toString(), etElaboracion.getText().toString(), etUrl.getText().toString(), v_foto, "N")
        //Volvemos atras y actualizarmos los datos al crear el MainActivity
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}