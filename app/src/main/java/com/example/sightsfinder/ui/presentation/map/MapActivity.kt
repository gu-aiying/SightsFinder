package com.example.sightsfinder.ui.presentation.map

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.sightsfinder.R
import com.example.sightsfinder.databinding.ActivityMapBinding
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.search.SearchFactory
import com.yandex.mapkit.search.SearchManager
import com.yandex.mapkit.search.SearchManagerType
import com.yandex.runtime.image.ImageProvider
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMapBinding
    private lateinit var searchManager: SearchManager
    private lateinit var mapObjects: MapObjectCollection
    private lateinit var userLocation: DoubleArray
    private lateinit var landmarkLocation: DoubleArray
    private lateinit var landmarkName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // !intent should be used in the block onCreate or later
        intent?.let { nonNullIntent ->
            userLocation = nonNullIntent.getDoubleArrayExtra("userLocation") ?: doubleArrayOf()
            landmarkLocation =
                nonNullIntent.getDoubleArrayExtra("landmarkLocation") ?: doubleArrayOf()
            landmarkName = nonNullIntent.getStringExtra("landmarkName") ?: ""
        } ?: run {
            finish() // if Intent is null，close Activity
            return
        }

        binding.mapview.mapWindow.map.move(
            CameraPosition(
                Point(userLocation[0], userLocation[1]),
                /* zoom = */ 5.0f,
                /* azimuth = */ 0.0f,
                /* tilt = */ 0.0f
            )
        )

        //initialize searchManager and mapObjectCollection
        searchManager = SearchFactory.getInstance()
            .createSearchManager(SearchManagerType.COMBINED) //online and offline map
        mapObjects = binding.mapview.mapWindow.map.mapObjects.addCollection()
    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        binding.mapview.onStart()
        binding.mapview.postDelayed(
            {
                showLocationOnMap(Point(landmarkLocation[0], landmarkLocation[1]))
            }, 500 // delayed 500ms to ensure the map is initialed
        )

    }

    override fun onStop() {
        binding.mapview.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }

//    private fun searchLocation(landmark: String) {
//        val searchOptions = SearchOptions().apply {
//            resultPageSize = 5 // количество результатов на одну страницу поиска.
//            searchTypes = SearchType.GEO.value // тип объектов для поиска
//        }

//        val europeBoundingBox = BoundingBox(
//            Point(34.5, -25.0),
//            Point(71.5, 60.0)
//        )
//        val geometry = Geometry.fromBoundingBox(europeBoundingBox)

//        searchManager.submit(
//            landmark,
////            geometry,
//            searchOptions,
//            object : Session.SearchListener {
//                override fun onSearchResponse(response: Response) {
//                    val items = response.collection.children
//                    if (items.isNotEmpty()) {
//                        val point = items[0].obj?.geometry?.get(0)?.point
//                        point?.let { showLocationOnMap(it) }
//                    }
//                }
//
//                override fun onSearchError(error: Error) {
//                    Log.e("MapActivity", "Search Failed: $error")
//                }
//
//            }
//        )
//    }


    private fun showLocationOnMap(point: Point) {
        mapObjects.clear() //clear the old place marks

        val placeMark = mapObjects.addPlacemark().apply {
            geometry = point
            setIcon(ImageProvider.fromResource(this@MapActivity, R.drawable.landmark))
        }

        placeMark.setText(
            landmarkName
//            TextStyle()
        )

        binding.mapview.mapWindow.map.move(
            CameraPosition(point, 15.0f, 0.0f, 0.0f),
            Animation(
                Animation.Type.SMOOTH,
                2.5f  //duration
            ),
            null
        )


    }

}