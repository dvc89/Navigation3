package com.example.nav3compose.ui.screens

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberUpdatedMarkerState

@Composable
fun ConfiguracionContentScreen() {
    val context = LocalContext.current
    var temperatura by remember { mutableFloatStateOf(20f) }
    var tieneSensorReal by remember { mutableStateOf(false) }

    // Ubicación de ejemplo para el mapa
    val ubicacionActual = remember { LatLng(37.9922, -1.1307) }
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(ubicacionActual, 14f)
    }
    val markerState = rememberUpdatedMarkerState(position = ubicacionActual)

    // Manejo del Sensor de Temperatura Ambiental (SensorManager)
    DisposableEffect(context) {
        val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val tempSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)

        tieneSensorReal = tempSensor != null

        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                event?.let {
                    if (it.sensor.type == Sensor.TYPE_AMBIENT_TEMPERATURE) {
                        temperatura = it.values[0]
                    }
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }

        if (tempSensor != null) {
            sensorManager.registerListener(
                listener,
                tempSensor,
                SensorManager.SENSOR_DELAY_UI
            )
        }

        onDispose {
            if (tempSensor != null) {
                sensorManager.unregisterListener(listener)
            }
        }
    }

    // Clasificación de temperatura: Frío (<15°C), Templado (15°-25°C), Soleado (>25°C)
    val estadoTexto: String
    val emojiEstado: String
    val colorEstado: Color

    if (temperatura < 15f) {
        estadoTexto = "Frío"
        emojiEstado = "❄️"
        colorEstado = Color(0xFF00B0FF)
    } else if (temperatura in 15f..25f) {
        estadoTexto = "Templado"
        emojiEstado = "⛅"
        colorEstado = Color(0xFFFFB300)
    } else {
        estadoTexto = "Soleado / Caluroso"
        emojiEstado = "☀️"
        colorEstado = Color(0xFFFF5722)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Sensor & Google Maps",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Mapa de Google Maps mostrando la temperatura en el marcador
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState
            ) {
                Marker(
                    state = markerState,
                    title = "$emojiEstado $estadoTexto",
                    snippet = "Temperatura actual: ${"%.1f".format(temperatura)} °C"
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Tarjeta con información de temperatura
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = emojiEstado,
                    fontSize = 50.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "$estadoTexto (${"%.1f".format(temperatura)} °C)",
                    style = MaterialTheme.typography.titleLarge,
                    color = colorEstado
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Controles de prueba en caso de no poseer sensor físico real
        if (!tieneSensorReal) {
            Text(
                text = "Sin sensor ambiental físico. Simula la temperatura:",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.outline
            )
            Spacer(modifier = Modifier.height(4.dp))
            Slider(
                value = temperatura,
                onValueChange = { temperatura = it },
                valueRange = 0f..40f
            )
        } else {
            Text(
                text = "Lectura del sensor ambiental en tiempo real.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}
