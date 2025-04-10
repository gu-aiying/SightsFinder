package com.example.sightsfinder.ui.presentation.main

import androidx.lifecycle.ViewModel
import com.example.sightsfinder.data.location.LocationService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val locationService: LocationService
) : ViewModel() {

    sealed class LocationState {
        object Loading : LocationState()
        data class Success(val location: String) : LocationState()
        data class Error(val message: String) : LocationState()
    }

    private val _locationState = MutableStateFlow<LocationState>(LocationState.Success("Не определено"))
    val locationState = _locationState.asStateFlow()

    fun startLocationUpdates() {
        _locationState.value = LocationState.Loading
        try {
            locationService.startLocationUpdates { location ->
                _locationState.value = LocationState.Success(location)
            }
        } catch (e: Exception) {
            _locationState.value = LocationState.Error("Ошибка определения местоположения")
        }
    }

    fun stopLocationUpdates() {
        locationService.stopLocationUpdates()
    }
}