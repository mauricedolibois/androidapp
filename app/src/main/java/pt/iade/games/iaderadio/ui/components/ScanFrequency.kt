package pt.iade.games.iaderadio.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.compose.AppTheme

@Composable
fun ScanFrequencyButton() {
    AppTheme {
        OutlinedButton(
            onClick = { },
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary ),
            modifier = Modifier
                .width(200.dp)
                .height(125.dp)
            ) {
            Text(
                text = "Scan\nFrequency",
                color = Color(
                    red = 1f,
                    green = 1f,
                    blue = 1f,
                    alpha = 1f
                ),
                fontSize = 17.sp,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewScanFrequencyButton() {
    ScanFrequencyButton()
}