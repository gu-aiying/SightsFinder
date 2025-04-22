package com.example.sightsfinder.ui.presentation.camera

import android.content.Intent
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.sightsfinder.databinding.ActivityCameraBinding
import com.example.sightsfinder.ui.presentation.result.ResultActivity
import dagger.hilt.android.AndroidEntryPoint

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

                Glide.with(this@CameraActivity)
                    .load(imageUrl)
    //              .placeholder()
    //              .error()
                    .into(binding.ivPreview)

                binding.buttonsLayout.visibility = VISIBLE

                binding.btYes.setOnClickListener {
                    val intent = Intent(this, ResultActivity::class.java).apply {
                        putExtra("imageUrl", imageUrl)
                    }
                    startActivity(intent)
                }

                binding.btNo.setOnClickListener {
                    binding.buttonsLayout.visibility = GONE
                    Glide.with(this@CameraActivity)
                        .clear(binding.ivPreview)
                }

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