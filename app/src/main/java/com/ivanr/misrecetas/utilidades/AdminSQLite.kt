package com.ivanr.misrecetas.utilidades

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class AdminSQLite(context: Context?, name: String, factory: SQLiteDatabase.CursorFactory?, version: Int) : SQLiteOpenHelper(context, name, factory, version) {
    val util = Util()

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("create table recetas(codigo INTEGER PRIMARY KEY autoincrement, \n" +
                " descripcion text,\n" +
                " ingredientes text,\n" +
                " elaboracion text,\n" +
                " foto text,\n" +
                " url text)")
        db.execSQL("create table recetas_his(receta int ,\n" +
                "    linea int ,\n" +
                "    fecha date,\n" +
                "    ingredientes text\n" +
                "    elbaoracion text,\n" +
                "    foto text)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
    }

    fun creaReceta(p_context: Context?, p_admin: AdminSQLite, p_descripcion: String, p_elaboracion: String, p_url:String) {
        val bd = p_admin.writableDatabase
        val receta = ContentValues()
        receta.put("descripcion", p_descripcion)
        receta.put("elaboracion", p_elaboracion)
        receta.put("url", p_url)
        bd.insert("recetas", null, receta)
        bd.close()
        util.mensaje (p_context,"Se cargaron los datos del artículo")
    }
    fun borrarReceta (p_context: Context?, p_admin: AdminSQLite, p_id_Receta: Int?) {
        util.mensaje (p_context, "Estoy en boton borrar. IdReceta:"+p_id_Receta)
        val bd = p_admin.writableDatabase
        var v_borrado = bd.delete("recetas", "codigo=${p_id_Receta}", null)
        //var v_borrado = bd.delete("recetas", null, null)
        bd.close()
        if (v_borrado == 1) {
            util.mensaje (p_context, "Se ha borrado la receta correctamente")
        }
    }
    fun consultar (p_admin: AdminSQLite, p_sql:String): Cursor? {
        val bd = p_admin.writableDatabase
        var fila = bd.rawQuery(p_sql, null)
        return fila
    }
}