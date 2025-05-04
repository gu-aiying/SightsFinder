package com.example.sightsfinder.ui.presentation.result

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View.GONE
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.sightsfinder.R
import com.example.sightsfinder.databinding.ActivityResultBinding
import com.example.sightsfinder.ui.presentation.map.MapActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    private val viewmodel: ResultViewModel by viewModels()
    private var userLocation = doubleArrayOf()
    private var landmarkLocation = doubleArrayOf()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val isSuccess = intent.getBooleanExtra("isSuccess", false)
        val fromMain = intent.getBooleanExtra("fromMain", false)

        if (!isSuccess) {
            binding.tvFail.visibility = VISIBLE
            binding.tvResultName.visibility = INVISIBLE
            binding.tvResultScore.visibility = INVISIBLE
            binding.tvResultDistance.visibility = INVISIBLE
            binding.tvResultDescription.visibility = INVISIBLE
            binding.btShowMap.visibility = INVISIBLE
            binding.tvResultAddress.visibility = INVISIBLE
            binding.ivLandmark.visibility = INVISIBLE
            binding.scrollViewDescription.visibility = INVISIBLE
        } else {
            val name = intent.getStringExtra("name")
            val score = intent.getStringExtra("score")

            if (!fromMain && score != null) {
                val possibility = "%.2f".format(score.toFloat() * 100)
                binding.tvResultScore.text = "Вероятность: $possibility%"
            } else {
                binding.tvResultScore.visibility = GONE
                val distance = intent.getIntExtra("distance", 0)
                binding.tvResultDistance.text = "Расстояние: $distance м"
            }
            binding.tvFail.visibility = GONE

            viewmodel.getLocationResult.onEach { result ->
                result?.fold(
                    onSuccess = {
                        binding.tvResultAddress.text =
                            "Адрес: ${it.latitude}, ${it.longitude}, \n${it.address}"
                        landmarkLocation = doubleArrayOf(it.latitude, it.longitude)
                        viewmodel.calculateDistance(landmarkLocation)
                    },
                    onFailure = {
                        Log.e("error", "${it.message}")
                        binding.tvResultAddress.text = "Адрес: не определен"
                    }
                )
            }.launchIn(lifecycleScope)

            if (!name.isNullOrBlank()) {
                binding.tvResultName.text = name
                this.lifecycleScope.launch {
                    viewmodel.searchLocationByName(this@ResultActivity, name)
                    viewmodel.getLandmarkInfo(name)
                }
            }

            viewmodel.distance.onEach { result ->
                result?.fold(
                    onSuccess = {
                        binding.tvResultDistance.text = "Растояние: $it"
                        userLocation = viewmodel.userLocation
                    },
                    onFailure = {
                        userLocation = viewmodel.userLocation
                        binding.tvResultDistance.text = "Растояние: $it"
                    }
                )
            }.launchIn(lifecycleScope)

            viewmodel.landmarkImage.onEach { imageUrl ->
                if (!imageUrl.isNullOrBlank()) {
                    Glide.with(this)
                        .load(imageUrl)
                        .placeholder(R.drawable.placeholder) // показывается, пока картинка загружается
                        .error(R.drawable.error_image)       // если загрузка не удалась (404, таймаут)
                        .fallback(R.drawable.no_image)       // если imageUrl == null
                        .into(binding.ivLandmark)
                }
            }.launchIn(lifecycleScope)

            viewmodel.landmarkDescription.onEach { description ->
//                binding.tvResultDescription.visibility = VISIBLE
                binding.tvResultDescription.text = description
            }.launchIn(lifecycleScope)

            binding.btShowMap.setOnClickListener {
                if (userLocation.isNotEmpty() && landmarkLocation.isNotEmpty() && !name.isNullOrBlank()) {
                    val intent =
                        Intent(this@ResultActivity, MapActivity::class.java).apply {
                            putExtra("userLocation", userLocation)
                            putExtra("landmarkLocation", landmarkLocation)
                            putExtra("landmarkName", name)
                        }
                    startActivity(intent)
                }
            }
        }
    }
}