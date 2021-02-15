package com.ivanr.misrecetas

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.ivanr.misrecetas.utilidades.AdminSQLite
import com.ivanr.misrecetas.actividades.ActividadNuevaReceta
import com.ivanr.misrecetas.clases.Receta
import com.ivanr.misrecetas.utilidades.Util


class MainActivity : AppCompatActivity() {
    var vg_seleccionado:Boolean = false
    val util = Util()
    var listaRecetas = ArrayList<Receta>()
    lateinit var lvRecetas : ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        asocia_controles()
        consultarRecetas ()
    }

    fun asocia_controles () {
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
            //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show()
            startActivity(Intent(this@MainActivity, ActividadNuevaReceta::class.java)) //.putExtras(getIntent().getExtras()));
        }

        lvRecetas = findViewById(R.id.lvRecetas) as ListView
        lvRecetas.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, position, id ->
            util.mensaje(this, "CLICK. Receta: " + listaRecetas[position].v_descripcion +
                    //", Item:" + adapterView.getItemAtPosition(position) +
                    ",Position:" + position + ", ITem: " + adapterView.getItemIdAtPosition(position) + "," + adapterView.getTag() + "," + adapterView.selectedItemPosition)
        }
        lvRecetas.onItemLongClickListener = AdapterView.OnItemLongClickListener() { adapterview, view, position, id ->
            util.mensaje(this, "CLICK LARGO. Receta: " + listaRecetas[position].v_descripcion)
            vg_seleccionado = true
            true
        }
        lvRecetas.setOnTouchListener(object : OnSwipeTouchListener(this@MainActivity, ) {
            override fun onSwipeDown() {
                super.onSwipeDown()
                consultarRecetas()
            }
        })
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
        var fila = admin.consultar(admin, "select codigo, descripcion, elaboracion from recetas order by codigo desc")
        listaRecetas.clear()
        if (fila != null) {
            if (fila.moveToFirst()) {
                do {
                    val id = fila.getInt(fila.getColumnIndex("codigo"))
                    val descripcion = fila.getString(fila.getColumnIndex("descripcion"))
                    val elaboracion = fila.getString(fila.getColumnIndex("elaboracion"))

                    listaRecetas.add(Receta(id, descripcion, null, elaboracion, null, null))

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
            var mReceta = recetasList[position]

            if (convertView == null) {
                view = layoutInflater.inflate(R.layout.receta, parent, false)
                vh = ViewHolder(view, position, mReceta.v_id)
                view.tag = vh
                //Log.i("JSA", "set Tag for ViewHolder, position: " + position)
            } else {
                view = convertView
                vh = view.tag as ViewHolder
            }

            //vh.ivImagen.drawable = mReceta.v_foto
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

    @SuppressLint("WrongViewCast")
    private class ViewHolder(view: View?, position: Int, p_id_receta: Int?) {
        val util = Util()
        val tvDescripcion: TextView
        val tvIndicaciones: TextView
        //val ivImagen: ImageView
        val btBorrar: ImageButton

        init {
            this.tvDescripcion = view?.findViewById(R.id.tvDescripcion) as TextView
            this.tvIndicaciones = view.findViewById(R.id.tvIndicaciones) as TextView
            //this.ivImagen = view?.findViewById(R.id.ivImagen) as ImageView
            this.btBorrar = view.findViewById(R.id.btBorrar) as ImageButton
            btBorrar.setOnClickListener()  {
                val admin = AdminSQLite(view.context, "recetas", null, 1)
                admin.borrarReceta(view.context, admin, p_id_receta)
                //util.mensaje (view.context, "Estoy en boton borrar. Posición:"+position+";IdReceta:"+p_id_receta)
            }
        }
    }

}