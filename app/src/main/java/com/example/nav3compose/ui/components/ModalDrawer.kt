package com.example.nav3compose.ui.components

import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch

@Composable
fun ModalDrawer(
    content: @Composable () -> Unit
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val opciones = listOf("Opción 1", "Opción 2", "Opción 3")

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text("Menú desplegable")
                opciones.forEach { opcion ->
                    NavigationDrawerItem(
                        label = { Text(opcion) },
                        selected = false,
                        onClick = { scope.launch { drawerState.close() } }
                    )
                }

            }
        }
    ) {
        content()
    }
}