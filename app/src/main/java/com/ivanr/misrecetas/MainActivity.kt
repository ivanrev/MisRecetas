package com.ivanr.misrecetas

import android.content.Intent
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import androidx.drawerlayout.widget.DrawerLayout
import com.ivanr.misrecetas.actividades.ActividadNuevaReceta
import com.ivanr.misrecetas.ui.main.SectionsAdapterMain
import com.ivanr.misrecetas.utilidades.Utilidades

class MainActivity : AppCompatActivity() {
    val util = Utilidades
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val sectionsPagerAdapter = SectionsAdapterMain(this, supportFragmentManager)
        val viewPager: ViewPager = findViewById(R.id.view_pager_det)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs_det)
        tabs.setupWithViewPager(viewPager)

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener {
            if (tabs.selectedTabPosition == 2) {
            //Añadir nueva categoria
            } else {
                startActivity(Intent(this@MainActivity, ActividadNuevaReceta::class.java))
            }
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

}