package com.example.sightsfinder.ui.presentation.result

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View.GONE
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.sightsfinder.databinding.ActivityResultBinding
import dagger.hilt.android.AndroidEntryPoint

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

        val isSuccess = intent.getBooleanExtra("isSuccess", false)

        if (!isSuccess) {
            binding.tvFail.visibility = VISIBLE
            binding.tvResultName.visibility = INVISIBLE
            binding.tvResultScore.visibility = INVISIBLE
            binding.tvResultDistance.visibility = INVISIBLE
            binding.tvResultDescription.visibility = INVISIBLE
            binding.btShowMap.visibility = INVISIBLE
        } else {
            val name = intent.getStringExtra("name")
            val score = intent.getStringExtra("score")
            if (score != null) {
                val possibility = "%.2f".format(score.toFloat() * 100)
                binding.tvResultName.text = name
                binding.tvResultScore.text = "Possibility: $possibility%"
            }
            binding.tvFail.visibility = GONE
        }
    }

}