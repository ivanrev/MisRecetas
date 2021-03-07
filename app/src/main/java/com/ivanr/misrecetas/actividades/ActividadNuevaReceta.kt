package com.ivanr.misrecetas.actividades

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.ivanr.misrecetas.MainActivity2
import com.ivanr.misrecetas.R
import com.ivanr.misrecetas.clases.Categoria
import com.ivanr.misrecetas.databinding.ActivityNuevaRecetaBinding
import com.ivanr.misrecetas.utilidades.AdminSQLite
import com.ivanr.misrecetas.utilidades.Parametros
import com.ivanr.misrecetas.utilidades.Utilidades

class ActividadNuevaReceta : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private lateinit var binding: ActivityNuevaRecetaBinding
    private val rParam = Parametros
    private val util = Utilidades
    var vg_imagen: Bitmap? = null
    var cl_categoria_sel:Categoria? = null
    lateinit var listaCategorias:ArrayList<Categoria>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNuevaRecetaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        asocia_controles ()
        carga_sppiner_categorias()
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode== RESULT_OK && requestCode == rParam.PHOTO_SELECTED) {
            val selectedImage = data!!.data
            findViewById<ImageView>(R.id.ivImagenReceta).setImageURI(selectedImage)
            vg_imagen = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage)
        }
    }
    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p_posicion: Int, p3: Long) {
        if (p0!!.id == R.id.spCategorias ) {
            cl_categoria_sel = listaCategorias.get(p_posicion)
        }
    }
    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

    fun asocia_controles () {
        binding.spCategorias.setOnItemSelectedListener (this)
        val fab: FloatingActionButton = findViewById(R.id.bt_Crear_Receta)
        fab.setOnClickListener {
            crearReceta()
        }
        binding.ibIrUrl.setOnClickListener { view ->
            var v_url = binding.etUrl.getText().toString()
            if (v_url != "" ) {
                util.navega_url(this, v_url)
            }
            else {
                util.mensaje_nuevo(view, "No hay URL para navegar")
            } }
        binding.btSeImagen.setOnClickListener { seleccionarImagen() }
    }
    fun carga_sppiner_categorias () {
        val admin = AdminSQLite(this, "recetas", null, rParam.VERSION_BD)
        var fila = admin.consultar(admin, "select codigo, orden, descripcion, foto from categorias order by orden desc")
        listaCategorias = admin.carga_lista_categorias(fila)
        var listaCategorias_desc = ArrayList<String>()
        for (i in listaCategorias) {
            listaCategorias_desc.add( i.descripcion)
        }
        var categoriasAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, listaCategorias_desc)
        categoriasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spCategorias.adapter = categoriasAdapter
    }
    
    fun seleccionarImagen() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, rParam.PHOTO_SELECTED)
    }
    fun crearReceta () {
        val admin = AdminSQLite(this, "recetas", null, rParam.VERSION_BD)
        var v_id_categoria: Int? = null

        admin.creaReceta(admin, binding.etDescripcion.getText().toString(), binding.etIndicaciones.getText().toString(), binding.etUrl.getText().toString(),
                                vg_imagen, "N", cl_categoria_sel?.id, "")
        util.mensaje_nuevo(binding.root.rootView, "Receta guardada correctamente")
        //Volvemos atras y actualizarmos los datos al crear el MainActivity
        //val intent = Intent(this, MainActivity2::class.java)
        //startActivity(intent)
        onBackPressed()
    }

}

