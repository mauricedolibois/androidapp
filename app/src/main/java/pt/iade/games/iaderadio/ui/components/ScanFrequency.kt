import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.compose.AppTheme
import com.example.compose.textLight
import pt.iade.games.iaderadio.models.ScanFrequencyViewModel

@Composable
fun ScanFrequency(viewModel: ScanFrequencyViewModel) {
    val frequency = viewModel.frequency

    AppTheme {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Scan Frequency",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = textLight, // Sets the color of the text to white
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text(
            text = String.format("%.1f MHz", frequency),
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold,
            color = textLight // Sets the color of the text to white
        )
    }
    }
}

@Preview(showBackground = true)
@Composable
fun ScanFrequencyPreview() {
}

