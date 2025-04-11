package com.example.sightsfinder.ui.presentation.result

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.sightsfinder.databinding.ActivityResultBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    private val viewmodel: ResultViewModel by viewModels()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Enable the back button in the action bar
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowHomeEnabled(true)
        }
        val image = intent.getStringExtra("imageUrl")

        lifecycleScope.launch {
            if (image != null) {
                viewmodel.classify(image)
                viewmodel.classifyResult?.onSuccess {
                    binding.tvResultName.text = it.name
                    binding.tvResultScore.text = "Possibility: " + (it.score * 100).toString() + "%"
                }?.onFailure {
                    binding.tvResultName.text = it.message
                    binding.tvResultScore.text = "Possibility: undefined"
                }
            } else {
                Log.e("image", "image url was not delivered correctly")
                Toast.makeText(
                    this@ResultActivity,
                    "Something wrong with the uploaded image, please try again",
                    LENGTH_SHORT
                ).show()
            }
            Glide.with(this@ResultActivity)
                .load(image)
//                .placeholder()
//                .error()
                .into(binding.ivResultLandmark)
        }
    }

}