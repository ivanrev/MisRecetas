package com.ivanr.misrecetas.listener

import com.ivanr.misrecetas.clases.ImagenesReceta

interface OnClick_DetImagen_Listener {
    fun onClick_detImagen(imagen:ImagenesReceta, position: Int)
}