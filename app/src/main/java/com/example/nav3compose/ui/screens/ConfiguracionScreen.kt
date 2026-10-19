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

@Composable
fun ConfiguracionContentScreen() {
    val context = LocalContext.current
    var temperatura by remember { mutableFloatStateOf(20f) }
    var tieneSensorReal by remember { mutableStateOf(false) }

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
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Sensor de Temperatura",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(
                modifier = Modifier.padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Ilustración / Emoji según la temperatura
                Text(
                    text = emojiEstado,
                    fontSize = 80.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "$estadoTexto (${"%.1f".format(temperatura)} °C)",
                    style = MaterialTheme.typography.titleLarge,
                    color = colorEstado
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Si el dispositivo o emulador no posee sensor de temperatura ambiental físico, mostramos un Slider para simularlo
        if (!tieneSensorReal) {
            Text(
                text = "Tu dispositivo no posee un sensor ambiental físico. Usa el deslizador para simular el cambio de temperatura:",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.outline
            )
            Spacer(modifier = Modifier.height(8.dp))
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
