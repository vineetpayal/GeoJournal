package com.vineet.geojournal.ui.addentry

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.util.Log
import androidx.core.location.LocationManagerCompat.isLocationEnabled
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
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class AddEntryViewModel @Inject constructor(
    private val journalRepository: JournalRepository,
    private val locationClient: FusedLocationProviderClient,
    @ApplicationContext private val context: Context
) : ViewModel() {

    //ui state for the text field
    private val _noteText = MutableStateFlow("")
    val noteText = _noteText.asStateFlow()

    //ui state for photo picker
    private val _selectedPhotoUri = MutableStateFlow<Uri?>(null)
    val selectedPhotoUri = _selectedPhotoUri.asStateFlow()

    fun onNoteChanged(newText: String) {
        _noteText.value = newText
    }

    fun onPhotoSelected(uri: Uri?) {
        uri?.let {
            try {
                // CRITICAL: Take persistable URI permission
                // This allows the app to access the URI even after restart
                context.contentResolver.takePersistableUriPermission(
                    it,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
                _selectedPhotoUri.value = it
            } catch (e: SecurityException) {
                // Handle the case where permission cannot be taken
                e.printStackTrace()
                _selectedPhotoUri.value = it
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun saveEntry() {
        viewModelScope.launch {
            //get current location
            val location = getCurrentLocation() ?: run {
                //if location is null(like gps is turned off)
                println("Location not available")
                return@launch
            }

            //create new entry object
            val newEntry = JournalEntry(
                text = _noteText.value,
                photoUri = _selectedPhotoUri.value?.toString(),
                latitude = location.latitude,
                longitude = location.longitude
            )
            Log.d("Uri_check", "saveEntry: " + selectedPhotoUri.value?.toString())
            //save to database
            journalRepository.addEntry(newEntry)

            //clear the ui
            _noteText.value = ""
            _selectedPhotoUri.value = null
        }
    }

    // This is the function that gets the location
    @SuppressLint("MissingPermission")
    private suspend fun getCurrentLocation(): Location? {
        // Check if GPS is enabled
        if (!isLocationEnabled(context.getSystemService(LocationManager::class.java))) {
            return null
        }

        // Get a fresh location, not a stale one
        return locationClient.getCurrentLocation(
            Priority.PRIORITY_HIGH_ACCURACY, CancellationTokenSource().token
        ).await()// Use await() to make it work with coroutines
    }
}