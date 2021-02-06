package com.ivanr.misrecetas

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class AdminSQLite(context: Context?, name: String, factory: SQLiteDatabase.CursorFactory?, version: Int) : SQLiteOpenHelper(context, name, factory, version) {
    val util = Util()

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("create table recetas(codigo int primary key, \n" +
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

    fun consultar (p_admin: AdminSQLite, p_sql:String): Cursor? {
        val bd = p_admin.writableDatabase

        var fila = bd.rawQuery(p_sql, null)
        return fila
    }
}