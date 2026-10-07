package com.example.nav3compose.ui.theme.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.nav3compose.ui.theme.components.ModalDrawer
import com.example.nav3compose.ui.theme.components.NavBar
import com.example.nav3compose.ui.theme.components.TopBar
import com.example.nav3compose.ui.theme.navigation.NavigationWrapper

@Composable
fun HomeScreen(
    nombre: String,
    onLogout: () -> Unit
) {
    ModalDrawer(){
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = { TopBar()},
            bottomBar = { NavBar() }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "Bienvenido, $nombre")
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = onLogout) {
                    Text(text = "Cerrar sesión")
                }
            }
        }
    }

}