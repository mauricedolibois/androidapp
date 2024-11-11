package pt.iade.games.iaderadio.ui.components.menu

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import com.example.compose.textLight

@Composable
fun MenuButton(
    text: String,
    modifier: Modifier,
    color: Color = Color(
        red = 1f,
        green = 1f,
        blue = 1f,
        alpha = 1f
    ),
    onClick: () -> Unit,
    fontSize: Int = 20
) {
    AppTheme {
        OutlinedButton(
            onClick = onClick,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
            modifier = modifier
                .padding(10.dp)
        ) {
            Text(
                text = text,
                color = color,
                fontSize = fontSize.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewButton() {
    Column {
        MenuButton("Scan\nFrequency", modifier = Modifier.height(125.dp).width(400.dp), onClick = {})
        MenuButton("Notes",modifier = Modifier.height(50.dp), color = textLight, onClick = {})
    }
}
