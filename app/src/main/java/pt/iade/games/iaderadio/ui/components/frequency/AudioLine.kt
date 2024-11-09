package pt.iade.games.iaderadio.ui.components.frequency
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.dp
import com.example.compose.inversePrimaryLight
import kotlin.math.sin

@Composable
fun AudioLine(
    modifier: Modifier = Modifier,
    waveHeight: Float = 20f,       // Amplitude of the wave
    waveLength: Float = 100f,      // Wavelength of the wave
    speed: Float = 1f,             // Speed of the wave animation
    segments: Int = 100            // Number of segments for smoothness
) {
    // Infinite animation for the phase shift of the wave
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val phaseShift by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2 * Math.PI.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = (2000 / speed).toInt(), easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = ""
    )

    Canvas(modifier = modifier) {
        val width = size.width
        val centerY = size.height / 2  // Center the wave vertically

        // Calculate segment length based on the total width and number of segments
        val segmentWidth = width / segments

        val path = Path().apply {
            moveTo(0f, centerY)  // Start from the left center
            for (i in 0..segments) {
                val x = i * segmentWidth
                val y = centerY + waveHeight * sin((x / waveLength) * 2 * Math.PI.toFloat() + phaseShift)
                lineTo(x, y)
            }
        }

        drawPath(
            path = path,
            color = if (waveHeight >= 30f) Color.Red else if(waveHeight>=20) Color.Yellow else inversePrimaryLight, // Orange color
            style = androidx.compose.ui.graphics.drawscope.Stroke(width = 3.dp.toPx())
        )
    }
}
