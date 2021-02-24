package com.ivanr.misrecetas.clases

import android.graphics.Bitmap
import java.util.*

class Nota {
    var v_id: Int
    var v_id_linea: Int
    var v_fecha: String
    var v_descripcion: String

    public constructor(p_id: Int, p_id_linea:Int, p_fecha: String, p_Descripcion: String) {
        v_id = p_id
        v_id_linea = p_id_linea
        v_fecha = p_fecha
        v_descripcion = p_Descripcion
    }
}