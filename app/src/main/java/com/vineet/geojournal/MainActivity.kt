package com.vineet.geojournal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.vineet.geojournal.ui.addentry.AddEntryScreen
import com.vineet.geojournal.ui.journal.JournalListScreen
import com.vineet.geojournal.ui.journal.MapScreen
import com.vineet.geojournal.ui.navigation.Screen
import com.vineet.geojournal.ui.theme.GeoJournalTheme
import dagger.hilt.android.AndroidEntryPoint


data class NavItem(val screen: Screen, val label: String, val icon: ImageVector)

val navItems = listOf(
    NavItem(Screen.JournalList, "Journal", Icons.AutoMirrored.Filled.List),
    NavItem(Screen.Map, "Map", Icons.Default.LocationOn),
    NavItem(Screen.AddEntry, "Add", Icons.Default.Add),
)

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GeoJournalTheme {

                val navController = rememberNavController()

                Scaffold(
                    bottomBar = {
                        NavigationBar {
                            val navBackStackEntry by navController.currentBackStackEntryAsState()
                            val currentDestination = navBackStackEntry?.destination

                            navItems.forEach { navItem ->
                                NavigationBarItem(
                                    icon = {
                                        Icon(
                                            navItem.icon,
                                            contentDescription = navItem.label
                                        )
                                    },
                                    label = { Text(navItem.label) },
                                    selected = currentDestination?.hierarchy?.any { it.route == navItem.screen.route } == true,
                                    onClick = {
                                        navController.navigate(navItem.screen.route) {
                                            popUpTo(navController.graph.findStartDestination().id) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                )

                            }

                        }
                    }
                ) { innerPadding ->

                    NavHost(
                        navController = navController,
                        startDestination = Screen.JournalList.route,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable(Screen.JournalList.route) {
                            JournalListScreen()
                        }
                        composable(Screen.Map.route) {
                            MapScreen()
                        }
                        composable(Screen.AddEntry.route) {
                            AddEntryScreen()
                        }
                    }

                }

            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    GeoJournalTheme {
        AddEntryScreen()
    }
}
