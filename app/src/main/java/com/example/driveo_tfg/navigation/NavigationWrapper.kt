package com.example.driveo_tfg.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.driveo_tfg.screens.InicioSesion
import com.example.driveo_tfg.screens.Registro
import com.example.driveo_tfg.screens.FlotaVehiculos
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun NavigationWrapper(navHostController: NavHostController) {

    // Configura el NavHost con las rutas de navegación
    NavHost(navController = navHostController, startDestination = "InicioSesion") {

        // Ruta para la pantalla de inicio de sesión
        composable("InicioSesion") {
            InicioSesion(
                navController = navHostController
            )
        }

        // Ruta para la pantalla de registro
        composable("Registro") {
            Registro(
                navController = navHostController,
                db = FirebaseFirestore.getInstance()
            )
        }

        // Ruta para la pantalla de FlotaVehiculos
        composable("FlotaVehiculos") {
            FlotaVehiculos() // Asegúrate de pasar el navController si es necesario en la pantalla
        }

    }
}
