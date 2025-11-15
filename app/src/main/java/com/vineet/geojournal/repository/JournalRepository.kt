package com.vineet.geojournal.repository

import com.vineet.geojournal.data.local.EntryDao
import com.vineet.geojournal.data.local.JournalEntry
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class JournalRepository @Inject constructor(
    private val entryDao: EntryDao
) {

    //This will be called by view model
    suspend fun addEntry(entry: JournalEntry) {
        entryDao.insert(entry)
    }
}