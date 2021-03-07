package com.ivanr.misrecetas.actividades

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ivanr.misrecetas.databinding.ActivityNuevaCategoriaBinding

class ActividadNuevaCategoria: AppCompatActivity() {
    private lateinit var binding: ActivityNuevaCategoriaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNuevaCategoriaBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}