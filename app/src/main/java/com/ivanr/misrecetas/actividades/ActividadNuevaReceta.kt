package com.ivanr.misrecetas.actividades

import android.content.Intent
import android.graphics.Bitmap
import android.os.AsyncTask
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.ivanr.misrecetas.R
import com.ivanr.misrecetas.clases.Categoria
import com.ivanr.misrecetas.databinding.ActivityNuevaRecetaBinding
import com.ivanr.misrecetas.utilidades.AdminSQLite
import com.ivanr.misrecetas.utilidades.Parametros
import com.ivanr.misrecetas.utilidades.Utilidades
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import java.io.IOException

class ActividadNuevaReceta : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    lateinit var binding: ActivityNuevaRecetaBinding
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
        binding.spCategorias.setOnItemSelectedListener(this)
        binding.btCrearReceta.setOnClickListener { crearReceta() }
        binding.ibIrUrl.setOnClickListener { view ->
            var v_url = binding.etUrl.getText().toString()
            if (v_url != "" ) {
                util.navega_url(this, v_url)
            }
            else {
                util.mensaje_nuevo(view, "No hay URL para navegar")
            } }
        binding.btSeImagen.setOnClickListener { seleccionarImagen() }
        binding.ibDescargaUrl.setOnClickListener {
            val webscrap = webScrap()
            webscrap.execute(binding.etUrl.text.toString()) }
    }
    fun carga_sppiner_categorias () {
        val admin = AdminSQLite(this, "recetas", null, rParam.VERSION_BD)
        var fila = admin.consultar(admin, "select codigo, orden, descripcion, foto from categorias order by orden desc")
        listaCategorias = admin.carga_lista_categorias(fila)
        var listaCategorias_desc = ArrayList<String>()
        for (i in listaCategorias) {
            listaCategorias_desc.add(i.descripcion)
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

    inner class webScrap : AsyncTask<String, Void, Void>() {
        lateinit var document: Document
        //private val activityReference: WeakReference<ActividadNuevaReceta> = WeakReference(context)
        override fun doInBackground(vararg params: String?): Void? {
            try {
                val url = params[0]
                document = Jsoup.connect(url).get()
                //val img: Element = document.select("video").first()
                //val resp: String = img.absUrl("src")
                //val activity = activityReference.get()
                //activity?.startDownloading(resp)
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return null
        }
        override fun onPostExecute(aVoid: Void?) {
            super.onPostExecute(aVoid)
            var html = document.html()
            var body = document.body()
            var v_hay_ingredientes: Boolean = false
            var divIngr:Element = Element("a")
            var divElaboracion:Element
            var h1 = body.select("h1").first()

            /*var divs: Elements = document.select("div")
            for (div in divs) {
                var h2s:Elements = div.select("h2")
                for (h2 in h2s) {
                    if (h2.text().contains("Ingredientes")) {
                        divIngr = div
                        v_hay_ingredientes = true
                    }
                    if (h2.text().contains("Elaboración")) {divElaboracion = div}
                }
            }*/
            var uls:Elements = document.select ("ul")
            var v_Lista_ingredientes = uls
            for (ul in uls) {
                var lis = ul.children()
                for (li in lis){
                    if (esIngrediente (li.text())) {
                        v_Lista_ingredientes = lis
                        v_hay_ingredientes = true
                        break
                    }
                }
            }
            var v_ingredientes:String = ""
            if ( v_hay_ingredientes ) {
                for (li in v_Lista_ingredientes) {
                    v_ingredientes = v_ingredientes + li.text()
                }
            }
            binding.etDescripcion.setText(h1.html())
            binding.etIngredientes.setText(v_ingredientes)
            //val activity = activityReference.get()
            //if (activity == null || activity.isFinishing) return
        }
    }
    fun esIngrediente (elementoLista:String) :Boolean {
        for (clave in rParam.PALABRAS_CLAVE_INGREDIENTES) {
            if (elementoLista.contains(clave)) {
                return true
            }
        }
        return false
    }

}


