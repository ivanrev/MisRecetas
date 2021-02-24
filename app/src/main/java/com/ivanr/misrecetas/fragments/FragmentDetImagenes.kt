
package com.ivanr.misrecetas.fragments

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.ivanr.misrecetas.R
import com.ivanr.misrecetas.actividades.ActividadNuevaReceta
import com.ivanr.misrecetas.adapters.ImagenesAdapter
import com.ivanr.misrecetas.adapters.RecetasAdapter
import com.ivanr.misrecetas.ui.det_Imagenes.ImagenesViewModel
import com.ivanr.misrecetas.utilidades.AdminSQLite
import com.ivanr.misrecetas.utilidades.Parametros

class FragmentDetImagenes : Fragment() {
    val rParam = Parametros
    private lateinit var imagenesViewModel: ImagenesViewModel
    lateinit var v_ar_imgenes:Array<ByteArray>
    var v_id_receta:Int = -1
    lateinit var admin: AdminSQLite
    lateinit var lvImagenes: ListView
    lateinit var imagenesAdapter: ImagenesAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        imagenesViewModel = ViewModelProvider(this).get(ImagenesViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_det_imagenes, container, false)
        lvImagenes = root.findViewById(R.id.lvImagenes)

        var parametros: Bundle? = getActivity()?.getIntent()?.getExtras()
        v_id_receta = parametros!!.getInt("id_receta")
        admin = AdminSQLite(activity, "recetas", null, rParam.VERSION_BD)
        consultarImagenes(v_id_receta)

        return root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode== AppCompatActivity.RESULT_OK && requestCode == rParam.PHOTO_SELECTED) {
            val selectedImage = data!!.data
            //findViewById<ImageView>(R.id.ivImagenReceta).setImageURI(selectedImage)
            //vg_imagen = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage)
            consultarImagenes(v_id_receta)
        }
    }

    companion object {
        fun newInstance() = FragmentDetImagenes().apply {}
    }

    fun consultarImagenes (p_id_receta:Int) {
        var fila = admin.consultar(admin,"select receta, linea, linea_nota, foto from recetas_img where receta = " + v_id_receta + " order by linea asc")
        var v_ar_imgenes = admin.carga_lista_imagenes(fila)
        imagenesAdapter = ImagenesAdapter(activity, v_ar_imgenes)
        lvImagenes.adapter = imagenesAdapter

    }
    fun seleccionarImagen() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, rParam.PHOTO_SELECTED)
    }
}