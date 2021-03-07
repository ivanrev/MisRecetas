package com.ivanr.misrecetas.clases

import android.graphics.Bitmap

class MaquinaCocina {
    var id:Int
    var descripcion:String
    var foto: Bitmap?

    constructor(p_id:Int, p_descripcion:String, p_foto: Bitmap?){
        id = p_id
        descripcion = p_descripcion
        foto = p_foto
    }
}