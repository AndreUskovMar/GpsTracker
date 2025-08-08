package ru.auskov.gpstracker.location

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import ru.auskov.gpstracker.location.data.LocationData
import javax.inject.Singleton

@Singleton
class LocationDataSharer() {
    private val _locationDataFlow = MutableSharedFlow<LocationData>()
    val locationFlow = _locationDataFlow.asSharedFlow()

    suspend fun updateLocation(locationData: LocationData) {
        _locationDataFlow.emit(locationData)
    }
}