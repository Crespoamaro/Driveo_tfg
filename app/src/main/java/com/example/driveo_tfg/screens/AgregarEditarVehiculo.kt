package com.example.driveo_tfg.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgregarEditarVehiculo(navController: NavHostController, vehiculoId: String? = null) {
    val db = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()
    val user = auth.currentUser
    var nombre by remember { mutableStateOf("") }
    var matricula by remember { mutableStateOf("") }
    var combustible by remember { mutableStateOf("") }
    var plataforma by remember { mutableStateOf("") }
    var horas by remember { mutableStateOf("") }
    val yellowColor = Color(0xFFF7FF62)

    // Estados para los menús desplegables
    var showPlataformaMenu by remember { mutableStateOf(false) }
    var showCombustibleMenu by remember { mutableStateOf(false) }
    val plataformas = listOf("Bolt", "Taxi", "Uber", "Cabify")
    val combustibles = listOf("Gasolina", "Diésel", "Eléctrico")

    val coroutineScope = rememberCoroutineScope()

    // Cargar datos si vehiculoId no es nulo
    LaunchedEffect(vehiculoId) {
        if (!vehiculoId.isNullOrEmpty()) {
            try {
                val snapshot = db.collection("vehiculos").document(vehiculoId).get().await()
                nombre = snapshot.getString("nombre") ?: ""
                matricula = snapshot.getString("matricula") ?: ""
                combustible = snapshot.getString("combustible") ?: ""
                plataforma = snapshot.getString("plataforma") ?: ""
                horas = snapshot.getString("horas") ?: ""
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (vehiculoId == null) "Agregar Vehículo" else "Editar Vehículo") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Regresar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = yellowColor)
            )
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre del conductor") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = matricula,
                    onValueChange = { matricula = it },
                    label = { Text("Matrícula") },
                    modifier = Modifier.fillMaxWidth()
                )

                // Selector de Plataforma
                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = plataforma,
                        onValueChange = {},
                        label = { Text("Plataforma") },
                        modifier = Modifier.fillMaxWidth(),
                        readOnly = true,
                        trailingIcon = {
                            IconButton(onClick = { showPlataformaMenu = true }) {
                                Icon(Icons.Default.ArrowDropDown, contentDescription = "Desplegar")
                            }
                        }
                    )
                    DropdownMenu(
                        expanded = showPlataformaMenu,
                        onDismissRequest = { showPlataformaMenu = false }
                    ) {
                        plataformas.forEach { plataformaItem ->
                            DropdownMenuItem(
                                text = { Text(plataformaItem) },
                                onClick = {
                                    plataforma = plataformaItem
                                    showPlataformaMenu = false
                                }
                            )
                        }
                    }
                }

                // Selector de Combustible
                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = combustible,
                        onValueChange = {},
                        label = { Text("Tipo de combustible") },
                        modifier = Modifier.fillMaxWidth(),
                        readOnly = true,
                        trailingIcon = {
                            IconButton(onClick = { showCombustibleMenu = true }) {
                                Icon(Icons.Default.ArrowDropDown, contentDescription = "Desplegar")
                            }
                        }
                    )
                    DropdownMenu(
                        expanded = showCombustibleMenu,
                        onDismissRequest = { showCombustibleMenu = false }
                    ) {
                        combustibles.forEach { combustibleItem ->
                            DropdownMenuItem(
                                text = { Text(combustibleItem) },
                                onClick = {
                                    combustible = combustibleItem
                                    showCombustibleMenu = false
                                }
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = horas,
                    onValueChange = { horas = it },
                    label = { Text("Horas de servicio") },
                    modifier = Modifier.fillMaxWidth()
                )

                Button(
                    onClick = {
                        coroutineScope.launch {
                            try {
                                val vehiculo = hashMapOf(
                                    "nombre" to nombre,
                                    "matricula" to matricula,
                                    "combustible" to combustible,
                                    "plataforma" to plataforma,
                                    "horas" to horas,
                                    "uid" to (user?.uid ?: "")
                                )

                                if (vehiculoId.isNullOrEmpty()) {
                                    // Agregar un nuevo vehículo
                                    db.collection("vehiculos").add(vehiculo).await()
                                } else {
                                    // Editar un vehículo existente
                                    db.collection("vehiculos").document(vehiculoId).set(vehiculo).await()
                                }
                                navController.popBackStack()
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = yellowColor)
                ) {
                    Text(text = if (vehiculoId == null) "Agregar" else "Guardar cambios", fontSize = 16.sp, color = Color.Black)
                }
            }
        }
    )
}