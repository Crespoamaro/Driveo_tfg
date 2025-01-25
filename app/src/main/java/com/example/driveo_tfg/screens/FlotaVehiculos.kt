package com.example.driveo_tfg.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.Image
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.driveo_tfg.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlotaVehiculos() {
    val yellowColor = Color(0xFFF7FF62) // Amarillo F7FF62

    // Simulación de datos
    val vehiculos = List(10) {
        Vehiculo(
            nombre = "Juan Antonio Gutierrez",
            matricula = "2541 MVZ",
            combustible = "Gasolina",
            horas = "8 horas"
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                actions = {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Menú",
                        modifier = Modifier.padding(end = 16.dp)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = yellowColor)
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(Color.White)
            ) {
                // Filtros
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    listOf("Uber", "Cabify", "Bolt", "Taxi").forEach { filtro ->
                        Button(
                            onClick = { /* Acción de filtro */ },
                            shape = RoundedCornerShape(20.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                        ) {
                            Text(text = filtro, color = Color.Black, fontSize = 12.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Barra de título y búsqueda
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Flota de Vehículos",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.Black
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Agregar",
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        BasicTextField(
                            value = "",
                            onValueChange = {},
                            modifier = Modifier
                                .background(
                                    color = Color(0xFFF1F1F1),
                                    shape = RoundedCornerShape(10.dp)
                                )
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                                .width(120.dp),
                            singleLine = true,
                            textStyle = TextStyle(color = Color.Black, fontSize = 14.sp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Buscar"
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Lista de vehículos
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(vehiculos) { vehiculo ->
                        VehiculoCard(vehiculo = vehiculo)
                    }
                }
            }
        }
    )
}

@Composable
fun VehiculoCard(vehiculo: Vehiculo) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* Acción al hacer clic en el vehículo */ },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(10.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Imagen del vehículo
            Image(
                painter = painterResource(id = R.drawable.corolla), // Reemplaza con tu recurso de imagen
                contentDescription = "Vehículo",
                modifier = Modifier
                    .size(60.dp)
                    .background(Color.LightGray, RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Información del vehículo
            Column(modifier = Modifier.weight(1f)) {
                Text(text = vehiculo.nombre, style = MaterialTheme.typography.bodyMedium)
                Text(text = vehiculo.matricula, fontSize = 14.sp, color = Color.Gray)
                Text(text = vehiculo.combustible, fontSize = 14.sp, color = Color.Gray)
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Acciones
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.Brightness5, contentDescription = "Detalles")
                Spacer(modifier = Modifier.width(8.dp))
                Icon(imageVector = Icons.Default.Edit, contentDescription = "Editar")
                Spacer(modifier = Modifier.width(8.dp))
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Eliminar")
            }
        }
    }
}

// Modelo de datos del vehículo
data class Vehiculo(
    val nombre: String,
    val matricula: String,
    val combustible: String,
    val horas: String
)