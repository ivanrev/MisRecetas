package com.ivanr.misrecetas.clases

import android.content.Context
import android.content.Context.PRINT_SERVICE
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.print.PrintAttributes
import android.print.PrintManager
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.core.content.FileProvider
import com.itextpdf.text.Document
import com.itextpdf.text.Image
import com.itextpdf.text.Paragraph
import com.itextpdf.text.factories.GreekAlphabetFactory.getString
import com.itextpdf.text.pdf.PdfPCell
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter
import com.ivanr.misrecetas.R
import com.ivanr.misrecetas.utilidades.Parametros
import com.ivanr.misrecetas.utilidades.Utilidades
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream

class Receta {
    private val rParam = Parametros
    private val util = Utilidades
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
                       p_categoria: Int?, p_maquina_cocinado: String?) {
        v_id = p_id
        v_descripcion = p_descripcion
        v_ingredientes = p_ingredientes
        v_elaboracion = p_elaboracion
        v_url = p_url
        v_favorito = p_favorito
        v_categoria = p_categoria
        v_maquina_cocinado = p_maquina_cocinado
    }
    fun put_foto(p_imagen: Bitmap?) {
        v_foto = p_imagen
    }
    fun get_foto (): Bitmap? {
        return v_foto
    }

    override fun toString(): String {
        return "Receta [id: ${this.v_id}, descripcion: ${this.v_descripcion}, ingredientes: ${this.v_ingredientes}, elaboracion: ${this.v_elaboracion}"+
                        ", url: ${this.v_url}, favorito: ${this.v_favorito}]"
    }

    fun to_html(p_receta: Receta): String {

/*        "<style type=""text/css"">"+rParam.v_css+"</style>"+
        "<img id=""img_cabecera"" src="+p_receta.get_foto()+"></img>"+
*/
        var v_html:String = "<html><head><title>"+p_receta.v_descripcion+"</title>"+
        "<h1>"+p_receta.v_descripcion+"</h1>"+
        "</head>"+
        "<body>"+
        "<div><h2>Ingredientes</h2><p>"+p_receta.v_ingredientes+"</p></div>"+
        "<div><h2>Elaboración</h2><p>"+p_receta.v_elaboracion+"</p></div>"+
        "<footer><p>Generado por Mis Recetas.</p></footer>"+
        "</body>"+
        "</html>"
        return v_html
    }
    fun receta_a_pdf(p_context: Context, p_receta:Receta):String {
        //var v_html:String = to_html(p_receta)
        val mDoc = Document ()
        val mFilePath = Environment.getExternalStorageDirectory().toString() + "/" + p_receta.v_descripcion+".pdf"
        try {
            PdfWriter.getInstance(mDoc, FileOutputStream(mFilePath))
            mDoc.open()
            mDoc.addAuthor("Mis Recetas")
            mDoc.addTitle(p_receta.v_descripcion)
            mDoc.add(Paragraph(p_receta.v_descripcion))

            var table = PdfPTable(1)
            var imagen = PdfPCell(Image.getInstance(util.img_to_array(p_receta.get_foto())))
            table.addCell(imagen)
            mDoc.add(table)
            mDoc.add(Paragraph("Ingredientes:"))
            mDoc.add(Paragraph(p_receta.v_ingredientes))
            mDoc.add(Paragraph("Elaboracion:"))
            mDoc.add(Paragraph(p_receta.v_elaboracion))
            mDoc.add(Paragraph("Generado por Mis Recetas."))
            mDoc.close()
        }
        catch (e: Exception){
            Log.i("LOG_FILE", e.message!!)
        }

        return mFilePath
    }

}