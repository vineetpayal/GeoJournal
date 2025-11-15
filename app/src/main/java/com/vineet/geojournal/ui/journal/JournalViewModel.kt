package com.vineet.geojournal.ui.journal

import android.content.Context
import android.location.Location
import android.location.LocationManager
import androidx.core.location.LocationManagerCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.vineet.geojournal.data.local.JournalEntry
import com.vineet.geojournal.repository.JournalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.osmdroid.util.GeoPoint
import javax.inject.Inject

@HiltViewModel
class JournalViewModel @Inject constructor(
    repository: JournalRepository,
    private val locationClient: FusedLocationProviderClient,
    @ApplicationContext private val context: Context
) : ViewModel() {

    val allEntries: StateFlow<List<JournalEntry>> = repository.getAllEntries().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000L),
        initialValue = emptyList()
    )

    private val _currentLocation = MutableStateFlow<GeoPoint?>(null)
    val currentLocation = _currentLocation.asStateFlow()


    fun fetchCurrentLocation() {
        viewModelScope.launch {
            val location = getCurrentLocation()
            if (location != null) {
                _currentLocation.value = GeoPoint(location.latitude, location.longitude)
            }
        }
    }

    @Suppress("MissingPermission")
    private suspend fun getCurrentLocation(): Location? {
        if (!LocationManagerCompat.isLocationEnabled(context.getSystemService(LocationManager::class.java))) {
            return null
        }

        return locationClient.getCurrentLocation(
            Priority.PRIORITY_HIGH_ACCURACY,
            CancellationTokenSource().token
        ).await()
    }
}