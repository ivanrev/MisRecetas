package com.ivanr.misrecetas.clases

import android.graphics.Bitmap

class ImagenesReceta {
    var v_id: Int
    var v_id_linea: Int
    var v_id_linea_nota: Int
    var v_foto: Bitmap?

    public constructor(p_id: Int, p_id_linea:Int, p_id_linea_nota: Int, p_foto:Bitmap?) {
        v_id = p_id
        v_id_linea = p_id_linea
        v_id_linea_nota = p_id_linea_nota
        v_foto = p_foto
    }
}