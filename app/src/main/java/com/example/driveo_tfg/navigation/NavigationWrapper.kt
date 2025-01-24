package com.example.driveo_tfg.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.driveo_tfg.screens.InicioSesion
import com.google.firebase.auth.FirebaseAuth

@Composable
fun NavigationWrapper(navHostController: NavHostController) {

    val auth = FirebaseAuth.getInstance()

    NavHost(navController = navHostController, startDestination = "InicioSesion") {

        // Modo día
        composable("InicioSesion") {
            InicioSesion(
                navController = navHostController,
                auth = auth
            )
        }
    }
}