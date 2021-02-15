package com.ivanr.misrecetas.actividades

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.ivanr.misrecetas.R
import com.ivanr.misrecetas.utilidades.AdminSQLite
import com.ivanr.misrecetas.utilidades.Util

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class ActividadNuevaReceta : AppCompatActivity() {
    val util = Util()
    lateinit var etDescripcion: TextView
    lateinit var etElaboracion:TextView
    lateinit var etUrl:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.nueva_receta)

        asociarControles()
    }

    fun asociarControles () {
        etDescripcion = findViewById(R.id.etDescripcion)
        etElaboracion = findViewById(R.id.etIndicaciones)
        etUrl = findViewById(R.id.etUrl)

        findViewById<Button>(R.id.bt_Crear_Receta).setOnClickListener {
            crearReceta()
        }
        findViewById<ImageButton>(R.id.ib_ir_url).setOnClickListener {
            var v_url = etUrl.getText().toString()
            if (v_url != "" ) {
                val uri: Uri = Uri.parse(v_url)
                val intent = Intent(Intent.ACTION_VIEW, uri)
                startActivity(intent)
            }
            else {
                util.mensaje(this, "No hay URL para navegar")
            }
        }

    }

    fun crearReceta () {
        val admin = AdminSQLite(this, "recetas", null, 1)
        admin.creaReceta(this, admin, etDescripcion.getText().toString(), etElaboracion.getText().toString(), etUrl.getText().toString())
        //Volvemos atras y actualizarmos los datos al crear el MainActivity
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}