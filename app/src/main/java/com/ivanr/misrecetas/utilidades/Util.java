package com.ivanr.misrecetas.utilidades;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Vibrator;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Util {
    static int ms_vibra = 1000;

    public void muestraOculta(View view){
        if (view.getVisibility() == View.VISIBLE) {
            view.setVisibility(View.INVISIBLE);
        } else {
            view.setVisibility(View.VISIBLE);
        }
    }

    public void mensaje(Context context, String txt) {
        //Toast toast2 = Toast.makeText(getApplicationContext(), "Toast con gravity", Toast.LENGTH_SHORT);
        Toast toast2 = Toast.makeText(context, txt, Toast.LENGTH_SHORT);
        toast2.setGravity(Gravity.CENTER | Gravity.LEFT, 0, 0);

        toast2.show();
    }

    public void OcultarTeclado(Context contexto, View item) {
        InputMethodManager inputManager = (InputMethodManager) contexto.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(item.getWindowToken(), 0);
    }

    public void vibra(Context context) {
        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(ms_vibra);
    }

    public void llamada(Context context, String telefono) {
        Intent llamada = new Intent(Intent.ACTION_CALL);
        llamada.setData(Uri.parse("tel:" + telefono));

        context.startActivity(llamada);
    }

    public void mail (Context context, String mailto, String cuerpo_mail) {
        Intent mail = new Intent(Intent.ACTION_SEND);
        mail.setType("text/html");// charset=utf-8");
        mail.putExtra(Intent.EXTRA_SUBJECT, "Las Cestinas de Li");

        mail.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(new StringBuilder()
                                                .append(cuerpo_mail).toString()));

        context.startActivity(mail);
    }

    public static boolean grabaBitmapPNG(Context context, Bitmap bmp, String nombreFichero) {
        FileOutputStream out = null;
        boolean ok = true;
        try {
            ContextWrapper cw = new ContextWrapper(context);
            // path to /data/data/yourapp/app_data/imageDir
            File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
            // Create imageDir
            File mypath=new File(directory,nombreFichero);
            out = new FileOutputStream(mypath);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
            // PNG is a lossless format, the compression factor (100) is ignored
        } catch (Exception e) {
            ok = false;
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                return ok;
            } catch (IOException e) {
                return false;
            }
        }
    }

    public static Bitmap cargaBitmap(String path, String nombreFichero) {
        try {
            File f=new File(path, nombreFichero);
            Bitmap bmp = BitmapFactory.decodeStream(new FileInputStream(f));
            return bmp;
        }
        catch (FileNotFoundException e)
        {
            return null;
        }
    }

    protected byte[] codificaImagenParaGrabar(Bitmap bitmapFoto) {
        if (bitmapFoto != null) {
            try {
                int bytes = bitmapFoto.getByteCount();

                ByteArrayOutputStream outputStream = new ByteArrayOutputStream(bytes);
                byte[] datos;
                int compresion = 10;
                do {
                    bitmapFoto.compress(Bitmap.CompressFormat.JPEG, 100 - compresion, outputStream);
                    datos = outputStream.toByteArray();
                    compresion += 10;
                    outputStream.reset();
                } while (datos.length > 500000 && compresion <= 100);
                outputStream.flush();
                outputStream.close();
                if (compresion > 90)
                    return null;
                else
                    return datos;
            } catch (Exception e) {
                return null;
            }
        } else {
            return null;
        }
    }


}
