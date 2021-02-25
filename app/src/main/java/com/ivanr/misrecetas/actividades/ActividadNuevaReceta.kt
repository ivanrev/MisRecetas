package com.ivanr.misrecetas.actividades

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.ivanr.misrecetas.MainActivity2
import com.ivanr.misrecetas.R
import com.ivanr.misrecetas.adapters.CategoriasAdapter
import com.ivanr.misrecetas.utilidades.AdminSQLite
import com.ivanr.misrecetas.utilidades.Parametros
import com.ivanr.misrecetas.utilidades.Utilidades


class ActividadNuevaReceta : AppCompatActivity() {
    val util = Utilidades
    private val rParam = Parametros
    lateinit var etDescripcion: TextView
    lateinit var etElaboracion:TextView
    lateinit var etUrl:TextView
    var vg_imagen: Bitmap? = null
    lateinit var spCategorias: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nueva_receta)
        etDescripcion = findViewById(R.id.etDescripcion)
        etElaboracion = findViewById(R.id.etIndicaciones)
        etUrl = findViewById(R.id.etUrl)
        spCategorias = findViewById(R.id.spCategorias)


        val admin = AdminSQLite(this, "recetas", null, rParam.VERSION_BD)
        var fila = admin.consultar(admin, "select codigo, orden, descripcion, foto from recetas_cat order by orden desc")
        var listaCategorias = admin.carga_lista_cat_Descripcion (fila)

        //var languages = arrayOf("English", "French", "Spanish", "Hindi", "Russian", "Telugu", "Chinese")
        var categoriasAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, listaCategorias)
        categoriasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spCategorias.adapter = categoriasAdapter
        val fab: FloatingActionButton = findViewById(R.id.bt_Crear_Receta)
        fab.setOnClickListener {
            crearReceta()
        }
        findViewById<ImageButton>(R.id.ib_ir_url).setOnClickListener {view->
            var v_url = etUrl.getText().toString()
            if (v_url != "" ) {
                util.navega_url(this, v_url)
            }
            else {
                util.mensaje_nuevo(view, "No hay URL para navegar")
            }
        }
        findViewById<Button>(R.id.btSeImagen).setOnClickListener {
            seleccionarImagen()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode== RESULT_OK && requestCode == rParam.PHOTO_SELECTED) {
            val selectedImage = data!!.data
            findViewById<ImageView>(R.id.ivImagenReceta).setImageURI(selectedImage)
            vg_imagen = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage)
        }
    }
    fun seleccionarImagen() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, rParam.PHOTO_SELECTED)
    }

    fun crearReceta () {
        val admin = AdminSQLite(this, "recetas", null, rParam.VERSION_BD)
        //    var vg_imagen_bitmap: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_imagen_vacio)
        admin.creaReceta(admin, etDescripcion.getText().toString(), etElaboracion.getText().toString(), etUrl.getText().toString(), vg_imagen, "N")
        //Volvemos atras y actualizarmos los datos al crear el MainActivity
        val intent = Intent(this, MainActivity2::class.java)
        startActivity(intent)
    }
}

