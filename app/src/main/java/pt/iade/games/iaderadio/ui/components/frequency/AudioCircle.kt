package pt.iade.games.iaderadio.ui.components.frequency

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
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
fun AudioCirlce(scale: Float, modifier: Modifier = Modifier, factor: Float = 0f) {
    val infiniteTransition = rememberInfiniteTransition(label = "")
    AppTheme {
        Box(modifier = modifier) {
            // Infinite transition for scaling animations
            val infiniteTransition = rememberInfiniteTransition(label = "scalingAnimation")

            // Define scaling animations for the three circles
            val scale1 by infiniteTransition.animateFloat(
                initialValue = 1.0f * scale,
                targetValue = if (factor == 0f) 1.2f * scale else 1.0f * scale + factor * 0.1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(
                        durationMillis = if (factor == 0f) 1200 else 100,
                        easing = if (factor == 0f) FastOutSlowInEasing else Ease
                    ),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "scale1Animation"
            )

            val scale2 by infiniteTransition.animateFloat(
                initialValue = 1.0f * scale,
                targetValue = if (factor == 0f) 1.4f * scale else 1.0f * scale + factor * 0.2f,
                animationSpec = infiniteRepeatable(
                    animation = tween(
                        durationMillis = if (factor == 0f) 1200 else 100,
                        easing = if (factor == 0f) FastOutSlowInEasing else Ease
                    ),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "scale2Animation"
            )

            val scale3 by infiniteTransition.animateFloat(
                initialValue = 1.0f * scale,
                targetValue = if (factor == 0f) 1.6f * scale else 1.0f * scale + factor * 0.3f,
                animationSpec = infiniteRepeatable(
                    animation = tween(
                        durationMillis = if (factor == 0f) 1200 else 100,
                        easing = if (factor == 0f) FastOutSlowInEasing else LinearEasing
                    ),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "scale3Animation"
            )

            Box(
                contentAlignment = androidx.compose.ui.Alignment.Center,
                modifier = Modifier
                    .size(200.dp)
                    .padding(20.dp)
            ) {
                // Draw three circles with their respective scaling animations
                ScalableCircle(scale = scale1, color = inversePrimaryLight, strokeWidth = 6.dp)
                ScalableCircle(
                    scale = scale2,
                    color = secondaryContainerLightMediumContrast,
                    strokeWidth = 4.dp
                )
                ScalableCircle(
                    scale = scale3,
                    color = secondaryLightMediumContrast,
                    strokeWidth = 3.dp
                )
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