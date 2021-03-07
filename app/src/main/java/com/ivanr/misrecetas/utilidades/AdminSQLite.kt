package com.ivanr.misrecetas.utilidades

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.Bitmap
import com.ivanr.misrecetas.clases.*
import java.util.*
import kotlin.collections.ArrayList

class AdminSQLite(context: Context?, name: String, factory: SQLiteDatabase.CursorFactory?, version: Int) : SQLiteOpenHelper(context, name, factory, version) {
    val util = Utilidades

    override fun onCreate(db: SQLiteDatabase) {
        //Tablas para version 1, de base de datos.
        db.execSQL("create table IF NOT EXISTS recetas (codigo INTEGER PRIMARY KEY autoincrement, \n" +
                " descripcion text,\n" +
                " ingredientes text,\n" +
                " elaboracion text,\n" +
                " foto blob,\n" +
                " url text,\n" +
                " favorito text)")
        db.execSQL("create table IF NOT EXISTS recetas_his (id_nota integer primary key autoincrement, \n" +
                " receta integer ,\n "+
                " fecha text,\n" +
                " notas text,\n" +
                " foto blob)")
        db.execSQL("create table IF NOT EXISTS recetas_img (id_imagen integer primary key autoincrement,\n" +
                " receta integer,\n" +
                " foto blob )")
        db.execSQL("create table IF NOT EXISTS categorias (codigo integer primary key autoincrement, \n" +
                " orden int, \n" +
                " descripcion text)")
        db.execSQL("create table IF NOT EXISTS maquinas (codigo INTEGER primary key autoincrement, \n" +
                    " descripcion text, \n" +
                    " foto blob )")
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
    }
    fun existeColumna (db: SQLiteDatabase, p_tabla:String, p_columna:String):Boolean {
        var existe = false
        var cursor:Cursor
        cursor = db.rawQuery("PRAGMA table_info("+ p_tabla +")", null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                var name = cursor.getString(cursor.getColumnIndex("name"));
                if (p_columna.equals(name, true)) {
                    existe = true
                    break
                }
            }
        }
        return existe;
    }
    fun actualiza_bd (p_admin: AdminSQLite) {
        var bd = p_admin.writableDatabase
        if (!existeColumna(bd, "categorias", "foto")) {
            bd.execSQL("alter table categorias add foto blob")
        }
        if (!existeColumna(bd, "recetas", "categoria")) {
            bd.execSQL("alter table recetas add categoria integer")
        }
        if (!existeColumna(bd, "recetas", "maquina_cocinado")) {
            bd.execSQL("alter table recetas add maquina_cocinado text")
        }
    }

    ////////////////    RECETAS     /////////////////
    fun creaReceta(p_admin: AdminSQLite, p_descripcion: String, p_elaboracion: String, p_url: String, p_foto: Bitmap?,
                   p_favorito: String, p_categoria:Int?, p_maquina_cocinado:String) {
        val bd = p_admin.writableDatabase
        val receta = ContentValues()
        var v_imagen_ba = util.img_to_array(p_foto)
        receta.put("descripcion", p_descripcion)
        receta.put("elaboracion", p_elaboracion)
        receta.put("url", p_url)
        receta.put("foto", v_imagen_ba)
        receta.put("favorito", p_favorito)
        receta.put("categoria", p_categoria)
        receta.put("maquina_cocinado", p_maquina_cocinado)
        bd.insert("recetas", null, receta)
        bd.close()
    }
    fun creaNotaReceta (p_admin: AdminSQLite, p_id_Receta: Int?, p_fecha: String, p_notas:String, p_foto:Bitmap?) {
        val bd = p_admin.writableDatabase
        val notaReceta = ContentValues()
        var v_imagen_ba = util.img_to_array(p_foto)
        notaReceta.put("receta", p_id_Receta)
        notaReceta.put("fecha", p_fecha)
        notaReceta.put("notas", p_notas)
        notaReceta.put("foto", v_imagen_ba)
        bd.insert("recetas_his", null, notaReceta)
        bd.close()
    }
    fun creaImagenReceta (p_admin: AdminSQLite, p_id_Receta: Int?, p_foto:Bitmap) {
        val bd = p_admin.writableDatabase
        val imagenReceta = ContentValues()
        var v_imagen_ba = util.img_to_array(p_foto)
        imagenReceta.put("receta", p_id_Receta)
        imagenReceta.put("foto", v_imagen_ba)
        bd.insert("recetas_img", null, imagenReceta)
        bd.close()
    }
    fun borrarReceta(p_admin: AdminSQLite, p_id_Receta: Int?) {
        val bd = p_admin.writableDatabase
        bd.delete("recetas", "codigo=${p_id_Receta}", null)
        bd.close()
    }
    fun consultar(p_admin: AdminSQLite, p_sql: String): Cursor? {
        val bd = p_admin.writableDatabase
        var fila = bd.rawQuery(p_sql, null)
        return fila
    }
    fun actualizar(p_admin: AdminSQLite, p_tabla: String, p_id_Receta: Int?, p_campos_string: Array<String?> , p_valores_string: Array<String?>) {
        val bd = p_admin.writableDatabase
        val receta = ContentValues()
        var v_Actualizar = "N"
        if (p_campos_string.size > 0) {
            var vCont = 0
            for (campo in p_campos_string) {
                if (campo != null) {
                    receta.put(campo, p_valores_string[vCont])
                    vCont++
                }
            }
            v_Actualizar = "S"
        }

        if (v_Actualizar == "S") {
            bd.update(p_tabla, receta, "codigo=${p_id_Receta}", null)
        }
    }
    fun carga_lista_recetas(fila: Cursor?, p_registros:Int):  ArrayList<Receta> {
        var listaRecetas = ArrayList<Receta>()
        var v_Cont:Int = 0

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
                    var categoria = fila.getInt(fila.getColumnIndex("categoria"))
                    var maquina_cocinado = fila.getString(fila.getColumnIndex("maquina_cocinado"))

                    if (elaboracion==null){elaboracion=""}
                    if (ingredientes == null) {ingredientes = ""}
                    if (url==null){url=""}
                    if (maquina_cocinado==null){maquina_cocinado=""}

                    var foto_bm = util.array_to_img(foto)
                    val vr_receta = Receta(id, descripcion, ingredientes, elaboracion, url, favorito, categoria, maquina_cocinado)
                    vr_receta.put_foto(foto_bm)
                    listaRecetas.add(vr_receta)
                    v_Cont ++
                    if (v_Cont == p_registros) { break }
                } while (fila.moveToNext() )
            }
        }
        return listaRecetas
    }
    fun carga_lista_imagenes (fila: Cursor?):ArrayList<ImagenesReceta>{
        var listaImagenes = ArrayList<ImagenesReceta>()
        listaImagenes.clear()

        if (fila!=null){
            if (fila.moveToFirst()) {
                do {
                    val id_linea = fila.getInt(fila.getColumnIndex("id_imagen"))
                    val id_receta = fila.getInt(fila.getColumnIndex("receta"))
                    var foto = fila.getBlob(fila.getColumnIndex("foto"))

                    var foto_bm = util.array_to_img(foto)
                    var v_Imagen_Receta = ImagenesReceta (id_receta, id_linea, foto_bm)
                    listaImagenes.add(v_Imagen_Receta)

                } while (fila.moveToNext() )
            }
        }
        return listaImagenes
    }
    fun carga_lista_notas (fila: Cursor?):ArrayList<Nota> {
        var listaNotas = ArrayList <Nota>()
        listaNotas.clear()
        if (fila!=null){
            if (fila.moveToFirst()) {
                do {
                    val id_receta = fila.getInt(fila.getColumnIndex("receta"))
                    val id_linea_nota = fila.getInt(fila.getColumnIndex("id_nota"))
                    val fecha = fila.getString(fila.getColumnIndex("fecha"))
                    var descripcion = fila.getString(fila.getColumnIndex("notas"))

                    var v_Nota = Nota (id_receta, id_linea_nota, fecha, descripcion)
                    listaNotas.add(v_Nota)

                } while (fila.moveToNext() )
            }
        }
        return listaNotas
    }

    //////////////   CATEGORIAS     //////////////////////
    fun creaCategoria (p_admin: AdminSQLite, pcl_categoria: Categoria) {
        val bd = p_admin.writableDatabase
        val categoria = ContentValues ()
        var v_imagen_ba = util.img_to_array(pcl_categoria.foto)
        categoria.put("orden", pcl_categoria.orden)
        categoria.put ("descripcion", pcl_categoria.descripcion)
        categoria.put("foto", v_imagen_ba)
        bd.insert ("categorias", null, categoria)
        bd.close ()
    }
    fun borrarCategoria (p_admin: AdminSQLite, p_id: Int?) {
        val bd = p_admin.writableDatabase
        bd.delete("categorias", "codigo=${p_id}", null)
        bd.close()
    }
    fun carga_lista_categorias (fila: Cursor?):ArrayList<Categoria> {
        var listaCategorias = ArrayList <Categoria>()
        listaCategorias.clear()
        if (fila!=null){
            if (fila.moveToFirst()) {
                do {
                    val id_categoria = fila.getInt(fila.getColumnIndex("codigo"))
                    val orden = fila.getInt(fila.getColumnIndex("orden"))
                    var descripcion = fila.getString(fila.getColumnIndex("descripcion"))
                    var v_foto = fila.getBlob(fila.getColumnIndex("foto"))
                    var v_foto_bm = util.array_to_img(v_foto)

                    var v_categoria = Categoria (id_categoria, orden, descripcion, v_foto_bm)
                    listaCategorias.add(v_categoria)

                } while (fila.moveToNext() )
            }
        }
        return listaCategorias
    }

    ////////////    MAQUINAS       /////////////
    fun creaMaquina (p_admin: AdminSQLite, pcl_maquina: MaquinaCocina) {
        val bd = p_admin.writableDatabase
        val maquina = ContentValues ()
        var v_imagen_ba = util.img_to_array(pcl_maquina.foto)
        maquina.put ("descripcion", pcl_maquina.descripcion)
        maquina.put("foto", v_imagen_ba)
        bd.insert ("maquinas", null, maquina)
        bd.close ()
    }
    fun borrarMaquina (p_admin: AdminSQLite, p_id: Int?) {
        val bd = p_admin.writableDatabase
        bd.delete("maquinas", "codigo=${p_id}", null)
        bd.close()
    }
    fun carga_lista_maquinas (fila: Cursor?):ArrayList<MaquinaCocina> {
        var listaMaquinas = ArrayList <MaquinaCocina>()
        listaMaquinas.clear()
        if (fila!=null){
            if (fila.moveToFirst()) {
                do {
                    val id_maquina = fila.getInt(fila.getColumnIndex("codigo"))
                    var descripcion = fila.getString(fila.getColumnIndex("descripcion"))
                    var v_foto = fila.getBlob(fila.getColumnIndex("foto"))
                    var v_foto_bm = util.array_to_img(v_foto)

                    var v_maquina = MaquinaCocina (id_maquina, descripcion, v_foto_bm)
                    listaMaquinas.add(v_maquina)

                } while (fila.moveToNext() )
            }
        }
        return listaMaquinas
    }

}