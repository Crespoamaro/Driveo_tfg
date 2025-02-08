package com.example.driveo_tfg.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.driveo_tfg.R
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehiculoDetalle(navController: NavHostController, vehiculoId: String) {
    val yellowColor = Color(0xFFF7FF62)
    val db = FirebaseFirestore.getInstance()
    var vehiculo by remember { mutableStateOf<Vehiculo?>(null) }

    // Cargar vehículo desde Firestore
    LaunchedEffect(vehiculoId) {
        try {
            val snapshot = db.collection("vehiculos").document(vehiculoId).get().await()
            vehiculo = snapshot.toObject(Vehiculo::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    vehiculo?.let { v ->
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Detalle del Vehículo") },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = yellowColor)
                )
            },
            bottomBar = {
                BottomAppBar(
                    containerColor = yellowColor,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = "Información del Vehículo",
                            fontSize = 16.sp,
                            color = Color.Black
                        )
                    }
                }
            },
            content = { padding ->
                Column(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.corolla),
                        contentDescription = "Vehículo",
                        modifier = Modifier
                            .size(200.dp)
                            .background(Color.LightGray, RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Text(text = "Nombre: ${v.nombre}", fontSize = 20.sp)
                    Text(text = "Matrícula: ${v.matricula}", fontSize = 18.sp)
                    Text(text = "Plataforma: ${v.plataforma}", fontSize = 18.sp)
                    Text(text = "Combustible: ${v.combustible}", fontSize = 18.sp)
                    Text(text = "Horas: ${v.horas}", fontSize = 18.sp)
                }
            }
        )
    }
}