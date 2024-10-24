package pt.iade.games.iaderadio.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
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
fun MenuButton(
    text: String,
    height: Int
) {
    AppTheme {
        OutlinedButton(
            onClick = {},
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
            modifier = Modifier
                .width(200.dp)
                .height(height.dp)
        ) {
            Text(
                text = text,
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
fun PreviewButton() {
    Column {
        MenuButton("Scan\nFrequency",125)
        MenuButton("Notes",50)
    }
}
