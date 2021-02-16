package com.ivanr.misrecetas.utilidades

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.ivanr.misrecetas.clases.Receta

class AdminSQLite(context: Context?, name: String, factory: SQLiteDatabase.CursorFactory?, version: Int) : SQLiteOpenHelper(context, name, factory, version) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("create table recetas(codigo INTEGER PRIMARY KEY autoincrement, \n" +
                " descripcion text,\n" +
                " ingredientes text,\n" +
                " elaboracion text,\n" +
                " foto BLOB,\n" +
                " url text,\n" +
                " favorito text)")
        db.execSQL("create table recetas_his(receta int ,\n" +
                "    linea int ,\n" +
                "    fecha date,\n" +
                "    ingredientes text\n" +
                "    elbaoracion text,\n" +
                "    foto text)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
    }

    fun creaReceta(p_context: Context?, p_admin: AdminSQLite, p_descripcion: String, p_elaboracion: String, p_url:String, p_foto: ByteArray, p_favorito: String) {
        val bd = p_admin.writableDatabase
        val receta = ContentValues()
        receta.put("descripcion", p_descripcion)
        receta.put("elaboracion", p_elaboracion)
        receta.put("url", p_url)
        receta.put("foto", p_foto)
        receta.put("favorito", p_favorito)
        bd.insert("recetas", null, receta)
        bd.close()
    }
    fun borrarReceta (p_context: Context?, p_admin: AdminSQLite, p_id_Receta: Int?) {
        val bd = p_admin.writableDatabase
        var v_borrado = bd.delete("recetas", "codigo=${p_id_Receta}", null)
        //var v_borrado = bd.delete("recetas", null, null)
        bd.close()
    }
    fun consultar (p_admin: AdminSQLite, p_sql:String): Cursor? {
        val bd = p_admin.writableDatabase
        var fila = bd.rawQuery(p_sql, null)
        return fila
    }
    fun actualizar (p_admin: AdminSQLite, p_tabla: String, p_campo: String, p_valor: String, p_id_Receta: Int?) {
        val bd = p_admin.writableDatabase
        val receta = ContentValues()
        receta.put(p_campo, p_valor)
        bd.update(p_tabla, receta, "codigo=${p_id_Receta}", null)
    }

    fun carga_lista_recetas (fila:Cursor?):  ArrayList<Receta> {
        var listaRecetas = ArrayList<Receta>()
        listaRecetas.clear()
        if (fila != null) {
            if (fila.moveToFirst()) {
                do {
                    val id = fila.getInt(fila.getColumnIndex("codigo"))
                    val descripcion = fila.getString(fila.getColumnIndex("descripcion"))
                    val elaboracion = fila.getString(fila.getColumnIndex("elaboracion"))
                    val image: ByteArray = fila.getBlob(fila.getColumnIndex("foto"))
                    val favorito = fila.getString(fila.getColumnIndex("favorito"))

                    listaRecetas.add(Receta(id, descripcion, null, elaboracion, null, null, favorito))

                } while (fila.moveToNext())
            }
        }
        return listaRecetas
    }
}