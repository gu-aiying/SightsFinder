package com.example.sightsfinder.ui.presentation.result

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.example.sightsfinder.databinding.ActivityResultBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding

//    @Inject lateinit var model: EuropeLandmark

    private lateinit var bitmap: Bitmap


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Enable the back button in the action bar
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowHomeEnabled(true)
        }

        // get imageUrl from cameraActivity and change it to bitmap
        val imageUrl = intent.getStringExtra("imageUrl")
        val uri = imageUrl!!.toUri()
        bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(uri))

    }

    override fun onDestroy() {

        super.onDestroy()
    }


}