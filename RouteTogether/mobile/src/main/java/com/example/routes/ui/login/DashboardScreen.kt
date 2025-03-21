package com.example.routes.ui.login

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun DashboardScreen(db: FirebaseFirestore, viewModel: MapViewModel) {
    val navController = rememberNavController()
    var selectedItem by rememberSaveable { mutableStateOf(0) }
    var showBottomBar by remember { mutableStateOf(true) }


    Scaffold(
        content = { paddingValues ->
            // Main content below the top bar
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                NavHost(navController, startDestination = "home") {
                    composable("home") {
                        HomeScreen(db,navController)
                        showBottomBar = true

                    }
                    composable("map") {
                        MapScreen(db,viewModel)
                        showBottomBar = true

                    }
                    composable("friends") {
                        FriendsScreen(db,navController)
                        showBottomBar = true

                    }
                    composable("homeScreen") {
                        InitialScreen(navController)
                        showBottomBar = false

                    }
                    composable("profile_screen/{friendEmail}") { backStackEntry ->
                        val friendEmail = backStackEntry.arguments?.getString("friendEmail")
                        if (friendEmail != null) {
                            ProfileScreen(friendEmail = friendEmail, db = db,navController=navController)
                        }
                    }
                    composable("route_details/{routeName}/{friendEmail}") { backStackEntry ->
                        val routeName = backStackEntry.arguments?.getString("routeName") ?: ""
                        val userEmail = backStackEntry.arguments?.getString("friendEmail") ?: ""
                        RouteDetailsScreen(routeName = routeName, userEmail = userEmail, db = db)
                    }
                }
            }
        },
        bottomBar = {
            if (showBottomBar) {
                BottomNavigationBar(
                    selectedItem = selectedItem,
                    onItemSelected = { index ->
                        selectedItem = index
                        when (index) {
                            0 -> navController.navigate("home")
                            1 -> navController.navigate("map")
                            2 -> navController.navigate("friends")
                        }
                    }
                )
            }
        }
    )
}

@Composable
fun HomeScreen() {
    Text("Welcome to your Dashboard!")
}

@Composable
fun NotificationsScreen() {
    Text("Notifications")
}

@Composable
fun ProfileScreen() {
    Text("Profile")
}

@Composable
fun BottomNavigationBar(selectedItem: Int, onItemSelected: (Int) -> Unit) {
    NavigationBar(
        modifier = Modifier.fillMaxWidth(),
        contentColor = MaterialTheme.colorScheme.primary
    ) {
        NavigationBarItem(
            selected = selectedItem == 0,
            onClick = { onItemSelected(0) },
            icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
            label = { Text("Home") }
        )
        NavigationBarItem(
            selected = selectedItem == 1,
            onClick = { onItemSelected(1) },
            icon = { Icon(Icons.Filled.LocationOn, contentDescription = "Map") },
            label = { Text("Map") }
        )
        NavigationBarItem(
            selected = selectedItem == 2,
            onClick = { onItemSelected(2) },
            icon = { Icon(Icons.AutoMirrored.Filled.List, contentDescription = "Friends") },
            label = { Text("Friends") }
        )
    }
}