package com.ivanr.misrecetas

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    val util = Util()
    var listaRecetas = ArrayList<Receta>()
    lateinit var lvRecetas : ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
            startActivity(Intent(this@MainActivity, ActividadNuevaReceta::class.java)) //.putExtras(getIntent().getExtras()));

        }

        lvRecetas = findViewById(R.id.lvRecetas) as ListView
        lvRecetas.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, position, id ->
            util.mensaje(this, "Receta: " + listaRecetas[position].v_descripcion)
        }

        consultarRecetas ()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun consultarRecetas () {
        val admin = AdminSQLite(this, "recetas", null, 1)
        var fila = admin.consultar(admin, "select descripcion, elaboracion from recetas order by codigo desc")
        listaRecetas.clear()
        if (fila != null) {
            if (fila.moveToFirst()) {
                do {
                    val descripcion = fila.getString(fila.getColumnIndex("descripcion"))
                    val elaboracion = fila.getString(fila.getColumnIndex("elaboracion"))

                    listaRecetas.add(Receta(descripcion, null, elaboracion, null, null))

                } while (fila.moveToNext())
            }
        }

        var recetasAdapter = RecetasAdapter(this, listaRecetas)
        lvRecetas.adapter = recetasAdapter
    }

    inner class RecetasAdapter : BaseAdapter {
        private var recetasList = ArrayList<Receta>()
        private var context: Context? = null

        constructor(context: Context, recetasList: ArrayList<Receta>) : super() {
            this.recetasList = recetasList
            this.context = context
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
            val view: View?
            val vh: ViewHolder

            if (convertView == null) {
                view = layoutInflater.inflate(R.layout.receta, parent, false)
                vh = ViewHolder(view)
                view.tag = vh
                //Log.i("JSA", "set Tag for ViewHolder, position: " + position)
            } else {
                view = convertView
                vh = view.tag as ViewHolder
            }

            var mReceta = recetasList[position]

            vh.tvDescripcion.text = mReceta.v_descripcion
            vh.tvIndicaciones.text = mReceta.v_elaboracion

            return view
        }

        override fun getItem(position: Int): Any {
            return recetasList[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return recetasList.size
        }
    }

    private class ViewHolder(view: View?) {
        val tvDescripcion: TextView
        val tvIndicaciones: TextView
        //val ivEdit: ImageView
        init {
            this.tvDescripcion = view?.findViewById(R.id.tvDescripcion) as TextView
            this.tvIndicaciones = view?.findViewById(R.id.tvIndicaciones) as TextView
            //this.ivEdit = view?.findViewById(R.id.ivEdit) as ImageView
        }
    }
}