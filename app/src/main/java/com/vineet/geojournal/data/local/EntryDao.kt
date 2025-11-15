package com.vineet.geojournal.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface EntryDao {

    @Insert
    suspend fun insert(entry: JournalEntry)

    //we used flow so that ui automatically updates whenever there is a change in entries.
    @Query("SELECT * FROM journal_entries ORDER BY timestamp DESC")
    fun getAllEntries(): Flow<List<JournalEntry>>
}