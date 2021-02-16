package com.ivanr.misrecetas.clases

class Receta {
    var v_id: Int? = null
    var v_descripcion: String? = null
    var v_ingredientes: String? = null
    var v_elaboracion: String? = null
    var v_url: String? = null
    var v_foto: String? = null
    var v_favorito: String? = null

    public constructor(p_id: Int, p_descripcion: String?, p_ingredientes: String?, p_elaboracion: String?, p_url: String?, p_foto: String?, p_favorito: String?) {
        this.v_id = p_id
        this.v_descripcion = p_descripcion
        this.v_ingredientes = p_ingredientes
        this.v_elaboracion = p_elaboracion
        this.v_url = p_url
        this.v_foto = p_foto
        this.v_favorito = p_favorito
    }

}