package pt.iade.games.iaderadio.models

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import java.lang.Math.toDegrees

class ScanFrequencyViewModel(context: Context) : ViewModel(), SensorEventListener {
    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    private val magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)

    private val minFrequency = 30.0
    private val maxFrequency = 120.0
    private val updateInterval = 200L // 0.2 seconds in milliseconds

    var frequency by mutableDoubleStateOf(100.0) // Current frequency
        private set

    private var lockedFrequency: Double? = null // Holds the frequency when locked
    private var gravity: FloatArray? = null
    private var geomagnetic: FloatArray? = null

    private var rollDirection: Int = 0 // 1 for right, -1 for left, 0 for straight
    private var updateJob: Job? = null // Coroutine job for frequency updates

    init {
        accelerometer?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
        }
        magnetometer?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
        }
    }

    fun lockFrequency(locked: Boolean) {
        if (locked) {
            lockedFrequency = frequency
            stopFrequencyUpdate()
        } else {
            lockedFrequency = null
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            when (it.sensor.type) {
                Sensor.TYPE_ACCELEROMETER -> gravity = it.values
                Sensor.TYPE_MAGNETIC_FIELD -> geomagnetic = it.values
            }

            if (gravity != null && geomagnetic != null && lockedFrequency == null) {
                val R = FloatArray(9)
                val I = FloatArray(9)
                if (SensorManager.getRotationMatrix(R, I, gravity, geomagnetic)) {
                    val orientation = FloatArray(3)
                    SensorManager.getOrientation(R, orientation)

                    // Get roll angle in degrees (-90 to 90 for left/right tilt)
                    val rollAngle = toDegrees(orientation[2].toDouble()).toFloat()

                    // Determine tilt direction
                    rollDirection = when {
                        rollAngle > 45 -> 1   // Tilted to the right
                        rollAngle < -45 -> -1 // Tilted to the left
                        else -> 0            // Straight position
                    }

                    if (rollDirection != 0) {
                        startFrequencyUpdate()
                    } else {
                        stopFrequencyUpdate()
                    }
                }
            }
        }
    }

    private fun startFrequencyUpdate() {
        if (updateJob == null || !updateJob!!.isActive) {
            updateJob = CoroutineScope(Dispatchers.Default).launch {
                while (true) {
                    delay(updateInterval)
                    updateFrequency()
                }
            }
        }
    }

    private fun stopFrequencyUpdate() {
        updateJob?.cancel()
        updateJob = null
    }

    private fun updateFrequency() {
        if (rollDirection > 0 && frequency < maxFrequency) {
            frequency += 1.0
        } else if (rollDirection < 0 && frequency > minFrequency) {
            frequency -= 1.0
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Not used
    }

    override fun onCleared() {
        super.onCleared()
        stopFrequencyUpdate()
        sensorManager.unregisterListener(this)
    }
}
