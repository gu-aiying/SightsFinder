package com.example.sightsfinder.ui.presentation.result

import android.content.Context
import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sightsfinder.domain.model.Coordinate
import com.example.sightsfinder.domain.model.GeocodingRequest
import com.example.sightsfinder.domain.model.GeocodingResult
import com.example.sightsfinder.domain.repository.GetUserLocation
import com.example.sightsfinder.domain.usecase.GetLandmarkLocationUseCase
import com.example.sightsfinder.domain.usecase.GetUserLocationUseCase
import com.example.sightsfinder.ui.presentation.main.MainViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResultViewModel @Inject constructor(
    private val getLandmarkLocationUseCase: GetLandmarkLocationUseCase,
    private val getUserLocationUseCase: GetUserLocationUseCase
) : ViewModel() {

    private var _getLocationResult = MutableStateFlow<Result<GeocodingResult>?>(null)
    val getLocationResult = _getLocationResult.asStateFlow()

    private var _distance = MutableStateFlow<Result<String>?>(null)
    val distance = _distance.asStateFlow()

    var userLocation = doubleArrayOf()

    suspend fun searchLocationByName(
        context: Context,
        query: String,
    ) {
        val geocodingRequest = GeocodingRequest(
            context = context,
            query = query
        )

        _getLocationResult.value = getLandmarkLocationUseCase(geocodingRequest)
    }

    suspend fun calculateDistance(landmarkLocation: DoubleArray) {
        val currentLocation: StateFlow<Coordinate?> = getUserLocationUseCase()
        val lan1 = currentLocation.value?.latitude
        val lon1 = currentLocation.value?.longitude
        val lan2 = landmarkLocation[0]
        val lon2 = landmarkLocation[1]
        if (lan1 != null && lon1 != null) {
            userLocation = doubleArrayOf(lan1, lon1)
            val results = FloatArray(1) //todo
            Location.distanceBetween(lan1, lon1, lan2, lon2, results)
            val distanceInKiloMeters = results[0] / 1000
            _distance.value = Result.success("%.2f".format(distanceInKiloMeters) + " km")
        } else {
            userLocation = doubleArrayOf(0.0, 0.0)
            _distance.value = Result.failure(Error("Fail to get user location"))
        }

    }


}