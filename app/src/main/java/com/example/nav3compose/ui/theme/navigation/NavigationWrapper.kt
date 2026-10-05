package com.example.nav3compose.ui.theme.navigation

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.example.nav3compose.ui.theme.screens.Home
import com.example.nav3compose.ui.theme.screens.HomeScreen
import com.example.nav3compose.ui.theme.screens.Login
import com.example.nav3compose.ui.theme.screens.LoginScreen

@Composable
fun NavigationWrapper() {
    val backStack = rememberNavBackStack(Login)
    NavDisplay(backStack = backStack,
        onBack = {
            backStack.removeLastOrNull()
        },
        entryProvider = entryProvider {
            entry <Login> {
                LoginScreen(
                    onLoginSuccess = { nombre ->
                        backStack.add(Home(nombre = nombre))
                    }
                )
            }
            entry <Home> { key ->
                HomeScreen(
                    nombre = key.nombre,
                    onLogout = {
                        backStack.removeLastOrNull()
                    }
                )
            }
        }
    )
}