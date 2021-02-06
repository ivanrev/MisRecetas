package com.ivanr.misrecetas

class Receta {
    var descripcion: String? = null
    var ingredientes: String? = null
    var elbaoracion: String? = null

    var url: String? = null

    constructor(descripcion: String, elaboracion: String, url: String) {
        this.descripcion = descripcion
        this.ingredientes = ingredientes
        this.elbaoracion = elbaoracion
        this.url = url
    }
}