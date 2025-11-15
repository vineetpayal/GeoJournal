package com.vineet.geojournal.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "journal_entries")
data class JournalEntry(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val text: String,
    val latitude: Double,
    val longitude: Double,
    val photoUri: String?, //storing the photo uri as string from photo picker.
    val timestamp: Long = System.currentTimeMillis()
)
