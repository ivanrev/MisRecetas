package com.ivanr.misrecetas.utilidades

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.Bitmap
import com.ivanr.misrecetas.clases.Receta

class AdminSQLite(context: Context?, name: String, factory: SQLiteDatabase.CursorFactory?, version: Int) : SQLiteOpenHelper(context, name, factory, version) {
    val util = Utilidades()

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("create table recetas(codigo INTEGER PRIMARY KEY autoincrement, \n" +
                " descripcion text,\n" +
                " ingredientes text,\n" +
                " elaboracion text,\n" +
                " foto blob,\n" +
                " url text,\n" +
                " favorito text)")
        db.execSQL("create table recetas_his(receta int ,\n" +
                "    linea int ,\n" +
                "    fecha date,\n" +
                "    ingredientes text\n" +
                "    elbaoracion text,\n" +
                "    foto blob)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
    }

    fun creaReceta(p_context: Context?, p_admin: AdminSQLite, p_descripcion: String, p_elaboracion: String, p_url: String, p_foto: Bitmap, p_favorito: String) {
        val bd = p_admin.writableDatabase
        val receta = ContentValues()
        var v_imagen_ba = util.img_to_array(p_foto)
        receta.put("descripcion", p_descripcion)
        receta.put("elaboracion", p_elaboracion)
        receta.put("url", p_url)
        receta.put("foto", v_imagen_ba)
        receta.put("favorito", p_favorito)
        bd.insert("recetas", null, receta)
        bd.close()
    }
    fun borrarReceta(p_context: Context?, p_admin: AdminSQLite, p_id_Receta: Int?) {
        val bd = p_admin.writableDatabase
        bd.delete("recetas", "codigo=${p_id_Receta}", null)
        bd.close()
    }
    fun consultar(p_admin: AdminSQLite, p_sql: String): Cursor? {
        val bd = p_admin.writableDatabase
        var fila = bd.rawQuery(p_sql, null)
        return fila
    }
    fun actualizar(p_admin: AdminSQLite, p_tabla: String, p_campo: String, p_valor: String, p_id_Receta: Int?) {
        val bd = p_admin.writableDatabase
        val receta = ContentValues()
        receta.put(p_campo, p_valor)
        bd.update(p_tabla, receta, "codigo=${p_id_Receta}", null)
    }

    fun carga_lista_recetas(fila: Cursor?):  ArrayList<Receta> {
        var listaRecetas = ArrayList<Receta>()
        var imagen: String = "a"

        listaRecetas.clear()
        if (fila != null) {
            if (fila.moveToFirst()) {
                do {
                    val id = fila.getInt(fila.getColumnIndex("codigo"))
                    val descripcion = fila.getString(fila.getColumnIndex("descripcion"))
                    var elaboracion = fila.getString(fila.getColumnIndex("elaboracion"))
                    var ingredientes = fila.getString(fila.getColumnIndex("ingredientes"))
                    val favorito = fila.getString(fila.getColumnIndex("favorito"))
                    var url = fila.getString(fila.getColumnIndex("url"))
                    var foto = fila.getBlob(fila.getColumnIndex("foto"))

                    if (elaboracion==null){elaboracion=""}
                    if (ingredientes == null) {ingredientes = ""}
                    if (url==null){url=""}

                    var foto_bm = util.array_to_img(foto)
                    val vr_receta = Receta(id, descripcion, ingredientes, elaboracion, url, favorito)
                    vr_receta.put_foto(foto_bm)
                    listaRecetas.add(vr_receta)
                } while (fila.moveToNext())
            }
        }
        return listaRecetas
    }

}