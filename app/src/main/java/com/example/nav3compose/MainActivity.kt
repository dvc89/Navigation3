package com.example.nav3compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.nav3compose.ui.theme.Nav3ComposeTheme
import com.example.nav3compose.ui.theme.navigation.NavigationWrapper

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Nav3ComposeTheme {
                NavigationWrapper()
            }
        }
    }
}
