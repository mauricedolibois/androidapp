package pt.iade.games.iaderadio.ui.components

import androidx.compose.foundation.border
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ScanFrequencyButton(
) {
    OutlinedButton(
        onClick = { },
        )    {
        Text(
            text = "Scan\nFrequency",
            textAlign = TextAlign.Center,
            color = Color(
                red = 0f,
                green = 0f,
                blue = 0f,
                alpha = 1f
            )
        )
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewScanFrequencyButton() {
    ScanFrequencyButton()
}