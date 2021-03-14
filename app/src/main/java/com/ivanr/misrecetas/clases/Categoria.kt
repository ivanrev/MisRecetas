package com.ivanr.misrecetas.clases

import android.graphics.Bitmap

class Categoria {
    var id:Int?
    var orden: Int?
    var descripcion:String
    var foto: Bitmap?

    constructor(p_id:Int?, p_orden:Int?, p_descripcion:String, p_foto:Bitmap?){
        id = p_id
        orden = p_orden
        descripcion = p_descripcion
        foto = p_foto
    }
}