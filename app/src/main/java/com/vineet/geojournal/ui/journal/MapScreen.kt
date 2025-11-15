// ui/journal/MapScreen.kt
package com.vineet.geojournal.ui.journal // Your package is correct

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.utsman.osmandcompose.Marker
import com.utsman.osmandcompose.OpenStreetMap
import com.utsman.osmandcompose.rememberCameraState
import com.utsman.osmandcompose.rememberMarkerState
import org.osmdroid.util.GeoPoint // Correct import
import org.osmdroid.views.overlay.Marker

@Composable
fun MapScreen(
    viewModel: JournalViewModel = hiltViewModel()
) {
    val allEntries by viewModel.allEntries.collectAsState()
    val currentLocation by viewModel.currentLocation.collectAsState()
    val context = LocalContext.current

    // This is the new state we need to prevent the race condition
    var isMapLoaded by remember { mutableStateOf(false) }

    val cameraState = rememberCameraState {
        geoPoint = GeoPoint(12.9716, 77.5946) // Default
        zoom = 12.0
    }

    // --- Permission Handling ---
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true) {
                viewModel.fetchCurrentLocation()
            }
        }
    )

    // --- Side Effects ---
    // 1. Check for location permission when the screen is first shown
    LaunchedEffect(Unit) {
        if (hasLocationPermission(context)) {
            viewModel.fetchCurrentLocation()
        } else {
            locationPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    // 2. This effect now waits for BOTH location and map readiness
    LaunchedEffect(currentLocation, isMapLoaded) {
        if (currentLocation != null && isMapLoaded) {
            cameraState.animateTo(currentLocation!!, 15.0)
        }
    }

    // --- UI ---
    OpenStreetMap(
        modifier = Modifier.fillMaxSize(),
        cameraState = cameraState,
        // This is the key: set our state to true when the map is ready
        onFirstLoadListener = {
            isMapLoaded = true
        }
    ) {
        // Markers
        allEntries.forEach { entry ->
            val entryGeoPoint = GeoPoint(entry.latitude, entry.longitude)
            Marker(
                state = rememberMarkerState(geoPoint = entryGeoPoint),
                title = entry.text.take(20) + "...",
            )
        }

        currentLocation?.let {
            Marker(
                state = rememberMarkerState(geoPoint = it),
                title = "Your Location"
            )
        }
    }
}

// Helper function
private fun hasLocationPermission(context: Context): Boolean {
    return ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED
}