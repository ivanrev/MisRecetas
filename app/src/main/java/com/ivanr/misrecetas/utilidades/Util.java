package com.ivanr.misrecetas.utilidades;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Vibrator;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.provider.Settings.System.getString;

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
    // convert from bitmap to byte array
    public static byte[] img_to_array(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    // convert from byte array to bitmap
    public static Bitmap array_t_img (byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

/*    private void imprimir (WebView webView, String p_nombre_app) {
        PrintManager printManager = (PrintManager) this.getSystemService(Context.PRINT_SERVICE);
        PrintDocumentAdapter printAdapter = webView.createPrintDocumentAdapter("MyDocument");
        String jobName = p_nombre_app  + " Print Test";
        printManager.print(jobName, printAdapter, new PrintAttributes.Builder().build());
    }*/


}
