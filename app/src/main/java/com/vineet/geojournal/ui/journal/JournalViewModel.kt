package com.vineet.geojournal.ui.journal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vineet.geojournal.data.local.JournalEntry
import com.vineet.geojournal.repository.JournalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class JournalViewModel @Inject constructor(
    repository: JournalRepository
) : ViewModel() {

    val allEntries: StateFlow<List<JournalEntry>> = repository.getAllEntries().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000L),
        initialValue = emptyList()
    )
}