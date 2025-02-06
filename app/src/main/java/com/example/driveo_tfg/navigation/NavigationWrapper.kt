package com.example.driveo_tfg.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.driveo_tfg.screens.AgregarEditarVehiculo
import com.example.driveo_tfg.screens.FlotaVehiculos
import com.example.driveo_tfg.screens.InicioSesion
import com.example.driveo_tfg.screens.Registro

@Composable
fun NavigationWrapper(navHostController: NavHostController) {
    NavHost(navController = navHostController, startDestination = "InicioSesion") {
        composable("InicioSesion") { InicioSesion(navHostController) }
        composable("Registro") { Registro(navHostController) }
        composable("FlotaVehiculos") { FlotaVehiculos(navHostController) }
        composable("AgregarVehiculo") { AgregarEditarVehiculo(navHostController) }
        composable("EditarVehiculo/{vehiculoId}") { backStackEntry ->
            AgregarEditarVehiculo(
                navHostController,
                backStackEntry.arguments?.getString("vehiculoId")
            )
        }
    }
}