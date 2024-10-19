package pt.iade.games.iaderadio.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.compose.AppTheme

@Composable
fun NotesButton() {
    AppTheme {
        OutlinedButton(
            onClick = {},
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
            modifier = Modifier
                .width(200.dp)
        ) {
            Text(
                text = "Notes",
                color = Color(
                    red = 1f,
                    green = 1f,
                    blue = 1f,
                    alpha = 1f
                ),
                fontSize = 20.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun previewButton() {
    NotesButton()
}