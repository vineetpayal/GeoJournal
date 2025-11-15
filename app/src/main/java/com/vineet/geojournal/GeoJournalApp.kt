package com.vineet.geojournal

import android.app.Application
import android.preference.PreferenceManager
import androidx.sqlite.db.SupportSQLiteOpenHelper
import dagger.hilt.android.HiltAndroidApp
import org.osmdroid.config.Configuration

@HiltAndroidApp
class GeoJournalApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Configuration.getInstance().load(
            this,
            PreferenceManager.getDefaultSharedPreferences(this)
        )

        Configuration.getInstance().userAgentValue = packageName
    }
}