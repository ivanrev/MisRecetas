package com.ivanr.misrecetas

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.ivanr.misrecetas.actividades.ActividadNuevaReceta
import com.ivanr.misrecetas.adapters.RecetasAdapter
import com.ivanr.misrecetas.clases.Receta
import com.ivanr.misrecetas.utilidades.AdminSQLite
import com.ivanr.misrecetas.utilidades.Util


class MainActivity : AppCompatActivity() {
    var vg_seleccionado:Boolean = false
    val util = Util()
    var listaRecetas = ArrayList<Receta>()
    lateinit var lvRecetas : ListView
    lateinit var recetasAdapter: RecetasAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        asocia_controles()
        asigna_eventos()
        consultarRecetas ()
    }

    fun asocia_controles () {
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
        lvRecetas = findViewById(R.id.lvRecetas)
    }

    fun asigna_eventos () {
        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
            startActivity(Intent(this@MainActivity, ActividadNuevaReceta::class.java)) //.putExtras(getIntent().getExtras()));
        }
        lvRecetas.onItemClickListener = AdapterView.OnItemClickListener { adapterView, v, position, id ->
            util.mensaje(this, listaRecetas[position].v_descripcion + "; Campo:" + v.id.toString())
            //util.mensaje(this, "CLICK. Receta: " + listaRecetas[position].v_descripcion +
            //", Item:" + adapterView.getItemAtPosition(position) +
            //        ",Position:" + position + ", ITem: " + adapterView.getItemIdAtPosition(position) + "," + adapterView.getTag() + "," + adapterView.selectedItemPosition)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        if (vg_seleccionado) {
            onBackPressed()
        }
        else {
            finish()
        }
    }

    fun consultarRecetas () {
        val admin = AdminSQLite(this, "recetas", null, 1)
        var fila = admin.consultar(admin, "select codigo, descripcion, elaboracion, foto, favorito from recetas order by codigo desc")
        var listaRecetas = admin.carga_lista_recetas (fila)
        recetasAdapter = RecetasAdapter(this, listaRecetas)
        lvRecetas.adapter = recetasAdapter
    }
}