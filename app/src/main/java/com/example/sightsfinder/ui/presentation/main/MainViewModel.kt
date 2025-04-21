package com.example.sightsfinder.ui.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sightsfinder.data.location.LocationService
import com.example.sightsfinder.domain.model.Landmark
import com.example.sightsfinder.domain.repository.LandmarkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val locationService: LocationService,
    private val landmarkRepository: LandmarkRepository
) : ViewModel() {

    sealed class LocationState {
        object Loading : LocationState()
        data class Success(val location: String) : LocationState()
        data class Error(val message: String) : LocationState()
    }

    private val _locationState = MutableStateFlow<LocationState>(LocationState.Success("Не определено"))
    val locationState = _locationState.asStateFlow()

    private val _landmarks = MutableStateFlow<List<Landmark>>(emptyList())
    val landmarks = _landmarks.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    fun startLocationUpdates() {
        _locationState.value = LocationState.Loading
        try {
            locationService.startLocationUpdates { locationStr  ->
                _locationState.value = LocationState.Success(locationStr )

                val parts = locationStr.split(", ")
                val lat = parts[0].toDoubleOrNull()
                val lon = parts[1].toDoubleOrNull()
                if (lat != null && lon != null) {
                    loadNearbyLandmarks(lat, lon)
                }
            }
        } catch (e: Exception) {
            _locationState.value = LocationState.Error("Ошибка определения местоположения")
        }
    }

    fun stopLocationUpdates() {
        locationService.stopLocationUpdates()
    }

    fun loadNearbyLandmarks(lat: Double, lon: Double) {
        viewModelScope.launch {
            _isLoading.value = true
            _landmarks.value = landmarkRepository.getNearbyLandmarks(lat, lon)
            _isLoading.value = false
        }
    }
}