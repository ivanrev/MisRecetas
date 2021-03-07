package com.ivanr.misrecetas.listener

import com.ivanr.misrecetas.clases.Receta

interface OnClick_ListReceta_Listener {
    fun onClick_listRecetas(receta: Receta, position: Int, p_accion:String)
}