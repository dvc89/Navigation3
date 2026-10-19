package com.example.nav3compose.ui.screens

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.nav3compose.domain.model.Usuario
import com.example.nav3compose.ui.viewModel.UsuarioViewModel

@Composable
fun InicioContentScreen(
    nombre: String,
    onLogout: () -> Unit,
    viewModel: UsuarioViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val usuarios by viewModel.usuarios.collectAsState()
    var usuarioAEditar by remember { mutableStateOf<Usuario?>(null) }
    var usuarioAEliminar by remember { mutableStateOf<Usuario?>(null) }

    // Solicitud de permiso de notificaciones en Android 13+ (API 33+)
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { _ -> }

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Bienvenido, $nombre",
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Usuarios registrados (${usuarios.size}):",
            modifier = Modifier.align(Alignment.Start)
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                items = usuarios,
                key = { it.id }
            ) { usuario ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = usuario.nombre,
                            )
                            Text(
                                text = "ID: ${usuario.id}",
                            )
                        }

                        Row {
                            IconButton(onClick = { usuarioAEditar = usuario }) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Editar usuario",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                            IconButton(onClick = { usuarioAEliminar = usuario }) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Eliminar usuario",
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onLogout) {
            Text(text = "Cerrar sesión")
        }
    }

    // Diálogo de confirmación de eliminación
    usuarioAEliminar?.let { usuario ->
        AlertDialog(
            onDismissRequest = { usuarioAEliminar = null },
            title = { Text(text = "Eliminar usuario") },
            text = { Text(text = "¿Estás seguro de que deseas eliminar a \"${usuario.nombre}\"?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.eliminarUsuario(usuario.id)
                        mostrarNotificacionUsuarioEliminado(context, usuario.nombre)
                        usuarioAEliminar = null
                    }
                ) {
                    Text(text = "Eliminar", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { usuarioAEliminar = null }) {
                    Text("Cancelar")
                }
            }
        )
    }

    // Diálogo de edición
    usuarioAEditar?.let { usuario ->
        var nuevoNombre by remember { mutableStateOf(usuario.nombre) }
        var nuevaContrasena by remember { mutableStateOf(usuario.contrasena) }

        AlertDialog(
            onDismissRequest = { usuarioAEditar = null },
            title = { Text(text = "Editar Usuario") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = nuevoNombre,
                        onValueChange = { nuevoNombre = it },
                        label = { Text("Nombre") },
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = nuevaContrasena,
                        onValueChange = { nuevaContrasena = it },
                        label = { Text("Contraseña") },
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (nuevoNombre.isNotBlank() && nuevaContrasena.isNotBlank()) {
                            viewModel.actualizarUsuario(
                                usuario.copy(
                                    nombre = nuevoNombre,
                                    contrasena = nuevaContrasena
                                )
                            )
                            usuarioAEditar = null
                        }
                    }
                ) {
                    Text("Guardar")
                }
            },
            dismissButton = {
                TextButton(onClick = { usuarioAEditar = null }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

// Función helper para crear y lanzar la notificación del sistema
private fun mostrarNotificacionUsuarioEliminado(context: Context, nombreUsuario: String) {
    val channelId = "usuarios_channel"
    val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    // Crear el Canal de Notificaciones (requerido para Android 8.0+)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            channelId,
            "Notificaciones de Usuarios",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Canal para notificaciones de eliminación de usuarios"
        }
        notificationManager.createNotificationChannel(channel)
    }

    // Construir la notificación
    val notification = NotificationCompat.Builder(context, channelId)
        .setSmallIcon(android.R.drawable.ic_menu_delete)
        .setContentTitle("Usuario eliminado")
        .setContentText("El usuario \"$nombreUsuario\" ha sido eliminado correctamente.")
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setAutoCancel(true)
        .build()

    notificationManager.notify(System.currentTimeMillis().toInt(), notification)
}
