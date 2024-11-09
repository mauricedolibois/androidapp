package pt.iade.games.iaderadio.ui.components.frequency

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.compose.AppTheme
import com.example.compose.outlineLight
import com.example.compose.primaryDark
import pt.iade.games.iaderadio.models.ScanFrequencyViewModel

@Composable
fun ScanFrequency(
    viewModel: ScanFrequencyViewModel,
    modifier: Modifier = Modifier,
    locked: Boolean = false
) {
    // Lock the frequency in the viewModel
    viewModel.lockFrequency(locked)

    // Display either the locked frequency or the current frequency from the viewModel
    val frequency = if (locked) viewModel.frequency else viewModel.frequency

    AppTheme {
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Scan Frequency",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = primaryDark,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Text(
                text = String.format("%.1f MHz", frequency),
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ScanFrequencyPreview() {
}
