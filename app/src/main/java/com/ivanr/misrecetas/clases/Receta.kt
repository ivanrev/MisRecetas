package com.ivanr.misrecetas.clases

import android.graphics.Bitmap
import com.google.gson.Gson

class Receta {

    var v_id: Int
    var v_descripcion: String?
    var v_ingredientes: String?
    var v_elaboracion: String?
    var v_url: String?
    var v_favorito: String?
    var v_foto: Bitmap? = null
    var v_categoria:Int?
    var v_maquina_cocinado:String?

    public constructor(p_id: Int, p_descripcion: String?, p_ingredientes: String?, p_elaboracion: String?, p_url: String?, p_favorito: String?,
                        p_categoria:Int?, p_maquina_cocinado:String?) {
        v_id = p_id
        v_descripcion = p_descripcion
        v_ingredientes = p_ingredientes
        v_elaboracion = p_elaboracion
        v_url = p_url
        v_favorito = p_favorito
        v_categoria = p_categoria
        v_maquina_cocinado = p_maquina_cocinado
    }
    fun put_foto (p_imagen: Bitmap?) {
        v_foto = p_imagen
    }
    fun get_foto (): Bitmap? {
        return v_foto
    }

    override fun toString(): String {
        return "Receta [id: ${this.v_id}, descripcion: ${this.v_descripcion}, ingredientes: ${this.v_ingredientes}, elaboracion: ${this.v_elaboracion}"+
                        ", url: ${this.v_url}, favorito: ${this.v_favorito}]"
    }


}