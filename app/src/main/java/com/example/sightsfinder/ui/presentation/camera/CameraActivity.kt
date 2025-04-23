package com.example.sightsfinder.ui.presentation.camera

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.sightsfinder.databinding.ActivityCameraBinding
import com.example.sightsfinder.ui.presentation.result.ResultActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CameraActivity : AppCompatActivity() {

    private val viewModel: CameraViewModel by viewModels()
    private lateinit var binding: ActivityCameraBinding
    private lateinit var imageBitmap: Bitmap
    private lateinit var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>

    private val requestCameraPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            takePicture.launch(null)
        } else {
            Toast.makeText(this, "Camera permission is needed", Toast.LENGTH_SHORT).show()
        }
    }

    private val takePicture =
        registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap: Bitmap? ->
            bitmap?.let {
                binding.ivPreview.setImageBitmap(it)
                binding.buttonsLayout.visibility = VISIBLE
                imageBitmap = it
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowHomeEnabled(true)
        }

        //registerForActivityResult() needs to be finished before onCreate or onStart
        pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
                uri?.let {
                    val uriToBitmap = viewModel.uriToBitmap(this, uri)
                    if (uriToBitmap != null) {
                        imageBitmap = uriToBitmap
                        binding.ivPreview.setImageBitmap(imageBitmap)
                        binding.buttonsLayout.visibility = VISIBLE
                    } else {
                        Toast.makeText(this, "Fail to process image", Toast.LENGTH_SHORT).show()
                    }
                }
            }

        binding.btYes.setOnClickListener {

            this.lifecycleScope.launch {
                viewModel.classify(imageBitmap)
                viewModel.classifyResult?.onSuccess {

                    val intent = Intent(this@CameraActivity, ResultActivity::class.java).apply {
                        putExtra("name", it.name)
                        putExtra("score", it.score)
                        putExtra("isSuccess", true)
                    }
                    startActivity(intent)
                }?.onFailure {
                    val intent = Intent(this@CameraActivity, ResultActivity::class.java).apply {
                        putExtra("isSuccess", false)
                    }
                    startActivity(intent)
                }
            }

        }

        binding.btNo.setOnClickListener {
            binding.buttonsLayout.visibility = GONE
            binding.ivPreview.setImageResource(0)
        }

        binding.btCamera.setOnClickListener {
            hasCameraPermission()
        }

        binding.btAlbum.setOnClickListener {

            // Launch the picker (will work without permissions on Android 13+)
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))

        }
    }

    private fun hasCameraPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            takePicture.launch(null)
        } else {
            requestCameraPermission.launch(android.Manifest.permission.CAMERA)
        }
    }


//    private fun showPermissionDeniedDialog() {
        // AlertDialog.Builder(this)
        //        .setTitle("需要相机权限")
        //        .setMessage("请前往设置允许相机权限")
        //        .setPositiveButton("去设置") { _, _ ->
        //            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        //                data = Uri.fromParts("package", packageName, null)
        //            }
        //            startActivity(intent)
        //        }
        //        .setNegativeButton("取消", null)
        //        .show()
//    }


}