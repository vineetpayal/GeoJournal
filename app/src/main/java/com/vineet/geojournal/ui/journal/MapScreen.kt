package com.vineet.geojournal.ui.journal

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.vineet.geojournal.data.local.JournalEntry
import kotlinx.coroutines.flow.StateFlow

@Composable
fun MapScreen(
    viewModel: JournalViewModel = hiltViewModel()
) {
    val allEntries by viewModel.allEntries.collectAsState()

    //users default camera position
    val defaultCameraPosition = LatLng(12.9716, 77.5946)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(defaultCameraPosition, 10f)
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    ) {
        //add a marker for each entry in the list
        allEntries.forEach { entry ->
            Marker(
                state = MarkerState(position = LatLng(entry.latitude, entry.longitude)),
                title = entry.text.take(20) + "...",
                snippet = "Click for details"
            )

        }
    }
}