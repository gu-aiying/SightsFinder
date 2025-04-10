package com.example.sightsfinder.ui.presentation.camera

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.sightsfinder.databinding.ActivityCameraBinding
import com.example.sightsfinder.ui.presentation.result.ResultActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CameraActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCameraBinding
    private lateinit var imageUrl: String
    private lateinit var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowHomeEnabled(true)
        }

        //registerForActivityResult() needs to be finished before onCreate or onStart
        pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                imageUrl = uri.toString()

                val intent = Intent(this, ResultActivity::class.java).apply {
                    putExtra("imageUrl", imageUrl)
                }
                lifecycleScope.launch {
                    delay(2000)
                }
                startActivity(intent)

            } else {
                // User didn't select any photo
            }
        }

        binding.btCamera.setOnClickListener {
            //todo
        }

        binding.btAlbum.setOnClickListener {

            // Launch the picker (will work without permissions on Android 13+)
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))

        }
    }

}