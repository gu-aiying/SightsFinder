package com.example.sightsfinder.ui.presentation.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sightsfinder.databinding.ActivityMainBinding
import com.example.sightsfinder.ui.presentation.camera.CameraActivity
import com.example.sightsfinder.ui.presentation.result.ResultActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: LandmarkAdapter
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

        viewModel.isLoading.onEach { loading ->
            binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
            binding.rvLandmarksNearbyList.visibility = if (loading) View.GONE else View.VISIBLE
        }.launchIn(lifecycleScope)

        requestPermissionLauncher.launch(
            arrayOf(
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )

        adapter = LandmarkAdapter { landmark ->
            val intent = Intent(this, ResultActivity::class.java).apply {
                putExtra("name", landmark.name)
                putExtra("score", "0.9")
                putExtra("isSuccess", true)
                putExtra("fromMain", true)
            }
            startActivity(intent)
        }

        binding.rvLandmarksNearbyList.layoutManager = LinearLayoutManager(this)
        binding.rvLandmarksNearbyList.adapter = adapter

        lifecycleScope.launch {
            viewModel.landmarks.collect { landmarks ->
                adapter.submitList(landmarks)
            }
        }
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

    override fun onStop() {
        super.onStop()

        viewModel.stopLocationUpdates()
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

