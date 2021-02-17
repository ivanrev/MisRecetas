package com.ivanr.misrecetas.clases

import android.graphics.Bitmap

class Receta  {
    var v_id: Int
    var v_descripcion: String?
    var v_ingredientes: String?
    var v_elaboracion: String?
    var v_url: String?
    var v_favorito: String?
    lateinit var v_foto: Bitmap

    public constructor(p_id: Int, p_descripcion: String?, p_ingredientes: String?, p_elaboracion: String?, p_url: String?, p_favorito: String?) {
        v_id = p_id
        v_descripcion = p_descripcion
        v_ingredientes = p_ingredientes
        v_elaboracion = p_elaboracion
        v_url = p_url
        v_favorito = p_favorito
    }
    fun put_foto (p_imagen: Bitmap) {
        v_foto = p_imagen
    }

}