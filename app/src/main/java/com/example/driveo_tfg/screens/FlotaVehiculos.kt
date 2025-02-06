package com.example.driveo_tfg.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.driveo_tfg.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlotaVehiculos(navController: NavHostController) {
    val yellowColor = Color(0xFFF7FF62)
    val db = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()
    val user = auth.currentUser
    var vehiculos by remember { mutableStateOf<List<Vehiculo>>(emptyList()) }
    var searchQuery by remember { mutableStateOf("") }
    var selectedVehiculo by remember { mutableStateOf<Vehiculo?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        user?.uid?.let { uid ->
            db.collection("vehiculos").whereEqualTo("uid", uid)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) return@addSnapshotListener
                    vehiculos = snapshot?.toObjects(Vehiculo::class.java) ?: emptyList()
                }
        }
    }

    fun eliminarVehiculo(id: String) {
        db.collection("vehiculos").document(id)
            .delete()
            .addOnSuccessListener {
                vehiculos = vehiculos.filter { it.id != id }  // Eliminamos de la lista
            }
            .addOnFailureListener { e ->
                coroutineScope.launch {
                    snackbarHostState.showSnackbar("Error al eliminar: ${e.message}")
                }
            }
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
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("AgregarVehiculo") },
                containerColor = yellowColor
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar vehículo")
            }
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(Color.White)
            ) {
                // Barra de búsqueda
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BasicTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        modifier = Modifier
                            .weight(1f)
                            .background(
                                color = Color(0xFFF1F1F1),
                                shape = RoundedCornerShape(10.dp)
                            )
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        singleLine = true,
                        textStyle = TextStyle(color = Color.Black, fontSize = 14.sp),
                        decorationBox = { innerTextField ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "Buscar"
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                innerTextField()
                            }
                        }
                    )
                }

                // Lista de vehículos
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(vehiculos.filter {
                        it.matricula.contains(searchQuery, true) ||
                                it.nombre.contains(searchQuery, true) ||
                                it.plataforma.contains(searchQuery, true)
                    }) { vehiculo ->
                        VehiculoCard(
                            vehiculo = vehiculo,
                            onEdit = { selectedVehiculo = vehiculo },
                            onDelete = { id -> eliminarVehiculo(id) }  // Llamamos a eliminarVehiculo
                        )
                    }
                }
            }
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    )

    selectedVehiculo?.let { vehiculo ->
        AlertDialog(
            onDismissRequest = { selectedVehiculo = null },
            title = { Text("Opciones del vehículo") },
            text = { Text("¿Qué acción deseas realizar?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        navController.navigate("EditarVehiculo/${vehiculo.id}")
                        selectedVehiculo = null
                    }
                ) {
                    Text("Editar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { selectedVehiculo = null }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
fun VehiculoCard(
    vehiculo: Vehiculo,
    onEdit: () -> Unit,
    onDelete: (String) -> Unit  // Ahora pasa el ID del vehículo
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* Acción al hacer clic */ },
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(10.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.corolla),
                contentDescription = "Vehículo",
                modifier = Modifier
                    .size(60.dp)
                    .background(Color.LightGray, RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = vehiculo.nombre, style = MaterialTheme.typography.bodyMedium)
                Text(text = vehiculo.matricula, fontSize = 14.sp, color = Color.Gray)
                Text(text = "Plataforma: ${vehiculo.plataforma}", fontSize = 14.sp, color = Color.Gray)
                Text(text = "Combustible: ${vehiculo.combustible}", fontSize = 14.sp, color = Color.Gray)
            }

            Row {
                IconButton(onClick = onEdit) {
                    Icon(Icons.Default.Edit, contentDescription = "Editar")
                }
                IconButton(onClick = { onDelete(vehiculo.id) }) {  // Llamamos a la función para eliminar
                    Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color.Red)
                }
            }
        }
    }
}

data class Vehiculo(
    val id: String = "",
    val nombre: String = "",
    val matricula: String = "",
    val plataforma: String = "",
    val combustible: String = "",
    val horas: String = "",
    val uid: String = ""
)