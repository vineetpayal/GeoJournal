package com.vineet.geojournal.ui.navigation

sealed class Screen(val route: String) {
    object JournalList : Screen("Journal_list")
    object Map : Screen("map")
    object AddEntry : Screen("add_entry")
}