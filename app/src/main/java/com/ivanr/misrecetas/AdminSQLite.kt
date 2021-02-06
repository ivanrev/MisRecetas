package com.ivanr.misrecetas

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class AdminSQLite(context: Context?, name: String, factory: SQLiteDatabase.CursorFactory?, version: Int) : SQLiteOpenHelper(context, name, factory, version) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("create table recetas(codigo int primary key, descripcion text, ingredientes text, indicaciones text)")
        db.execSQL("create table recetas_lin(receta int primary key, linea int primary key, ingrediente text, cantidad real, tipo_medicion text)")
        db.execSQL("create table recetas_his(receta int primary key, linea int primary key, fecha date, ingrediente text, cantidad real, tipo_medicion text)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

    }
}