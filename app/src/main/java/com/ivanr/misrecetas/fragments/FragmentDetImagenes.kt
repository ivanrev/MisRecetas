
package com.ivanr.misrecetas.fragments

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.ivanr.misrecetas.R
import com.ivanr.misrecetas.adapters.ImagenesAdapter
import com.ivanr.misrecetas.clases.ImagenesReceta
import com.ivanr.misrecetas.listener.OnClick_DetImagen_Listener
import com.ivanr.misrecetas.ui.det_Imagenes.ImagenesViewModel
import com.ivanr.misrecetas.utilidades.AdminSQLite
import com.ivanr.misrecetas.utilidades.Parametros
import com.ivanr.misrecetas.utilidades.Utilidades

class FragmentDetImagenes : Fragment(), OnClick_DetImagen_Listener {
    private var columnCount = 2
    private var v_id_receta:Int = -1

    private val rParam = Parametros
    private val util = Utilidades
    private lateinit var admin: AdminSQLite
    private lateinit var v_ar_imagenes:ArrayList<ImagenesReceta>
    private lateinit var imagenesViewModel: ImagenesViewModel
    private lateinit var recycler: RecyclerView
    private lateinit var imagenesAdapter: ImagenesAdapter
    private lateinit var root : View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        root = asocia_controles(inflater, container)
        carga_parametros()
        return root
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode== AppCompatActivity.RESULT_OK && requestCode == rParam.PHOTO_SELECTED) {
            val selectedImage = data!!.data
            var vg_imagen = MediaStore.Images.Media.getBitmap (this.context?.contentResolver, selectedImage)
            grabaImagen (vg_imagen)
            consultarImagenes(recycler, v_id_receta)
        }
    }
    override fun onClick_detImagen(imagen: ImagenesReceta, position: Int) {
        util.mensaje_nuevo(root, "Imagen:"+imagen.v_id.toString()+","+imagen.v_id_linea.toString())
    }
    companion object {
        fun newInstance() = FragmentDetImagenes().apply {}
    }

    fun asocia_controles (inflater: LayoutInflater, container: ViewGroup?):View {
        imagenesViewModel = ViewModelProvider(this).get(ImagenesViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_det_imagenes, container, false)
        recycler = root.findViewById(R.id.recycler_detImagenes)
        recycler.layoutManager = when {
            columnCount <= 1 -> LinearLayoutManager(context)
            else -> GridLayoutManager(context, columnCount)
        }
        root.findViewById<FloatingActionButton>(R.id.bt_detimg_anadir).setOnClickListener {
            seleccionarImagen()
        }
        return root
    }
    fun carga_parametros () {
        var parametros: Bundle? = getActivity()?.getIntent()?.getExtras()
        v_id_receta = parametros!!.getInt("id_receta")
        admin = AdminSQLite(activity, "recetas", null, rParam.VERSION_BD)
        consultarImagenes(recycler, v_id_receta)
    }
    fun consultarImagenes (recycler:RecyclerView, p_id_receta:Int) {
        var fila = admin.consultar(admin,"select id_imagen, receta, foto from recetas_img where receta = " +
                                                    p_id_receta + " order by id_imagen asc")
        v_ar_imagenes = admin.carga_lista_imagenes(fila)
        imagenesAdapter = ImagenesAdapter(v_ar_imagenes, this)
        recycler.adapter = imagenesAdapter
    }
    fun seleccionarImagen() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, rParam.PHOTO_SELECTED)
    }
    fun grabaImagen (p_imagen: Bitmap) {
        admin.creaImagenReceta (admin, v_id_receta, p_imagen)
    }
}