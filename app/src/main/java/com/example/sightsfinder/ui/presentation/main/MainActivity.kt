package com.example.sightsfinder.ui.presentation.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.sightsfinder.databinding.ActivityMainBinding
import com.example.sightsfinder.ui.presentation.camera.CameraActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()

        binding.btDetect.setOnClickListener {
            // Navigate to the camera screen
            val intent = Intent(this, CameraActivity::class.java)
            startActivity(intent)
        }
    }


}

