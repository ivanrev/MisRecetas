package com.ivanr.misrecetas.utilidades

import android.app.PendingIntent.getActivity
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Vibrator
import android.provider.MediaStore
import android.text.Html
import android.view.Gravity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.ivanr.misrecetas.clases.Receta
import java.io.*

object Utilidades {
    var ms_vibra = 1000

    fun muestraOculta(view: View) {
        if (view.visibility == View.VISIBLE) {
            view.visibility = View.INVISIBLE
        } else {
            view.visibility = View.VISIBLE
        }
    }

    fun mensaje_nuevo(view: View, p_mensaje: String) {
        Snackbar.make(view, p_mensaje, Snackbar.LENGTH_LONG).setAction("Action", null).show()
    }
    fun mensaje(context: Context?, txt: String?) {
        //Toast toast2 = Toast.makeText(getApplicationContext(), "Toast con gravity", Toast.LENGTH_SHORT);
        val toast2 = Toast.makeText(context, txt, Toast.LENGTH_SHORT)
        toast2.setGravity(Gravity.CENTER or Gravity.LEFT, 0, 0)
        toast2.show()
    }

    fun OcultarTeclado(contexto: Context, item: View) {
        val inputManager = contexto.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(item.windowToken, 0)
    }

    fun vibra(context: Context) {
        val v = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        v.vibrate(ms_vibra.toLong())
    }

    fun llamada(context: Context, telefono: String) {
        val llamada = Intent(Intent.ACTION_CALL)
        llamada.data = Uri.parse("tel:$telefono")
        context.startActivity(llamada)
    }

    fun mail(context: Context, p_asunto: String, p_cuerpo_mail: String?) {
        val mail = Intent(Intent.ACTION_SEND)
        mail.type = "text/html" // charset=utf-8");
        mail.putExtra(Intent.EXTRA_SUBJECT, p_asunto)
        mail.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(StringBuilder().append(p_cuerpo_mail).toString()))
        context.startActivity(mail)
    }

    fun getImageUriFromBitmap(context: Context, bitmap: Bitmap): Uri{
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(context.contentResolver, bitmap, "Title", null)
        return Uri.parse(path.toString())
    }

    fun img_to_array(bitmap: Bitmap?): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.PNG, 0, stream)
        return stream.toByteArray()
    }

    fun array_to_img(image: ByteArray?): Bitmap? {
        if (image!= null) {
            return BitmapFactory.decodeByteArray(image, 0, image.size)
        } else return null
    }

    fun string_to_Bytearray (file: String): ByteArray? {
        var bos: ByteArrayOutputStream? = null
        try {
            val f = File(file)
            val fis = FileInputStream(f)
            val buffer = ByteArray(1024)
            bos = ByteArrayOutputStream()
            var len: Int
            while (fis.read(buffer).also { len = it } != -1) {
                bos.write(buffer, 0, len)
            }
        } catch (e: FileNotFoundException) {
            //System.err.println(e.getMessage())
        } catch (e2: IOException) {
            //System.err.println(e2.getMessage())
        }
        return if (bos != null) bos.toByteArray() else null
    }

    fun receta_to_json (p_receta: Receta): String {
        var gson = Gson()
        var jsonString = gson.toJson(p_receta)
        return jsonString
    }
    fun json_to_receta (p_json: String): Receta {
        var gson = Gson()
        var jsonString = p_json
        var v_receta = gson.fromJson (jsonString, Receta::class.java)

        return v_receta
    }

    fun navega_url (p_context: Context, p_url:String) {
        val uri: Uri = Uri.parse(p_url)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        p_context.startActivity(intent)
    }
    /*    private void imprimir (WebView webView, String p_nombre_app) {
        PrintManager printManager = (PrintManager) this.getSystemService(Context.PRINT_SERVICE);
        PrintDocumentAdapter printAdapter = webView.createPrintDocumentAdapter("MyDocument");
        String jobName = p_nombre_app  + " Print Test";
        printManager.print(jobName, printAdapter, new PrintAttributes.Builder().build());
    }*/
}