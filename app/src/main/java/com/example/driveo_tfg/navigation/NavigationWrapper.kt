package com.example.driveo_tfg.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.driveo_tfg.screens.*
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun NavigationWrapper(navHostController: NavHostController) {
    NavHost(navController = navHostController, startDestination = "InicioSesion") {
        composable("InicioSesion") { InicioSesion(navHostController) }
        composable("Registro") { Registro(navHostController, FirebaseFirestore.getInstance()) }
        composable("FlotaVehiculos") { FlotaVehiculos(navHostController) }
        composable("AgregarVehiculo") { AgregarEditarVehiculo(navHostController) }
        composable("EditarVehiculo/{vehiculoId}") { backStackEntry ->
            AgregarEditarVehiculo(
                navHostController,
                backStackEntry.arguments?.getString("vehiculoId")
            )
        }
        composable("VehiculoDetalle/{vehiculoId}") { backStackEntry ->
            VehiculoDetalle(
                navHostController,
                backStackEntry.arguments?.getString("vehiculoId") ?: ""
            )
        }
    }
}