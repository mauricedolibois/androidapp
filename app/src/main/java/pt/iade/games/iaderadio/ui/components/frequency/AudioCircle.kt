package pt.iade.games.iaderadio.ui.components.frequency
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.compose.AppTheme
import com.example.compose.inversePrimaryLight
import com.example.compose.secondaryContainerLightMediumContrast
import com.example.compose.secondaryLightMediumContrast

@Composable
fun AudioCirlce(scale: Float, modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "")
    AppTheme {
        Box(modifier = modifier) {
            // Create different scale animations for each circle layer
            val scale1 by infiniteTransition.animateFloat(
                initialValue = 1.0f * scale,
                targetValue = 1.2f * scale,
                animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis = 1200, easing = FastOutSlowInEasing),
                    repeatMode = RepeatMode.Reverse
                ), label = ""
            )

            val scale2 by infiniteTransition.animateFloat(
                initialValue = 1.0f * scale,
                targetValue = 1.4f * scale,
                animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis = 1200, easing = FastOutSlowInEasing),
                    repeatMode = RepeatMode.Reverse
                ), label = ""
            )

            val scale3 by infiniteTransition.animateFloat(
                initialValue = 1.0f * scale,
                targetValue = 1.6f * scale,
                animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis = 1200, easing = FastOutSlowInEasing),
                    repeatMode = RepeatMode.Reverse
                ), label = ""
            )

            Box(
                contentAlignment = androidx.compose.ui.Alignment.Center,
                modifier = Modifier
                    .size(200.dp)
                    .padding(20.dp)
            ) {
                // Draw three circles with different scale effects
                ScalableCircle(scale = scale1, color = inversePrimaryLight, strokeWidth = 6.dp)
                ScalableCircle(scale = scale2, color = secondaryContainerLightMediumContrast, strokeWidth = 4.dp)
                ScalableCircle(scale = scale3, color = secondaryLightMediumContrast, strokeWidth = 3.dp)
            }
        }
    }
}

@Composable
fun ScalableCircle(scale: Float, color: Color, strokeWidth: Dp) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        // Scale the circle's drawing based on the animated scale factor
        scale(scale = scale) {
            drawCircle(
                color = color,
                radius = size.minDimension / 4f,
                style = Stroke(width = strokeWidth.toPx())
            )
        }
    }
}

