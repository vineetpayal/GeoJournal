package com.vineet.geojournal.ui.addentry

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage

@Composable
fun AddEntryScreen(
    viewModel: AddEntryViewModel = hiltViewModel()
) {

    //collect ui state from view model
    val noteText by viewModel.noteText.collectAsState()
    val selectedPhotoUri by viewModel.selectedPhotoUri.collectAsState()

    //permission handling
    var hasLocationPermission by remember { mutableStateOf(false) }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            hasLocationPermission =
                permissions[android.Manifest.permission.ACCESS_FINE_LOCATION] == true

        }
    )

    //photo picker handling
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri: Uri? ->
            viewModel.onPhotoSelected(uri)
        }
    )

    //Launch permission request when screen is first composed
    LaunchedEffect(Unit) {
        locationPermissionLauncher.launch(
            arrayOf(
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )

    }


    //ui
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Create New Journal Entry")

            OutlinedTextField(
                value = noteText,
                onValueChange = { viewModel.onNoteChanged(it) },
                label = { Text("What's on your mind?") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    photoPickerLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                }
            ) {
                Text("Pick Photo")
            }

            // Show the selected photo
            if (selectedPhotoUri != null) {
                AsyncImage(
                    model = selectedPhotoUri,
                    contentDescription = "Selected photo",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { viewModel.saveEntry() },
                // Only enable the "Save" button if permission is granted
                // and the user has written some text
                enabled = hasLocationPermission && noteText.isNotBlank(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (hasLocationPermission) "Save Entry" else "Location Permission Needed")
            }
        }
    }


}
