package com.example.sightsfinder.ui.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sightsfinder.data.location.LocationService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
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

    fun fetchLocation() {
        viewModelScope.launch {
            _locationState.value = LocationState.Loading
            val loc = locationService.getCurrentLocation()
            if (loc != null) {
                _locationState.value = LocationState.Success(loc)
            } else {
                _locationState.value = LocationState.Error("Местоположение недоступно")
            }
        }
    }
}