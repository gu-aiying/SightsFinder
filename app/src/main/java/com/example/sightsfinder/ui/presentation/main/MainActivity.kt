package com.example.sightsfinder.ui.presentation.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.sightsfinder.databinding.ActivityMainBinding
import com.example.sightsfinder.ui.presentation.camera.CameraActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.locationState.onEach { state ->
            when (state) {
                is MainViewModel.LocationState.Loading -> binding.tvGps.text = "Загрузка..."
                is MainViewModel.LocationState.Success -> binding.tvGps.text = state.location
                is MainViewModel.LocationState.Error -> binding.tvGps.text = state.message
            }
        }.launchIn(lifecycleScope)

        requestPermissionLauncher.launch(
            arrayOf(
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    override fun onStart() {
        super.onStart()

        viewModel.startLocationUpdates()

        binding.btDetect.setOnClickListener {
            // Navigate to the camera screen
            val intent = Intent(this, CameraActivity::class.java)
            startActivity(intent)
        }
    }


    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            if (permissions[android.Manifest.permission.ACCESS_FINE_LOCATION] == true) {
                viewModel.startLocationUpdates()
            } else {
                binding.tvGps.text = "Отказано в разрешении"
            }
        }
}

