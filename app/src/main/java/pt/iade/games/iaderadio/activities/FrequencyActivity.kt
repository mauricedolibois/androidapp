package pt.iade.games.iaderadio.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.compose.AppTheme
import com.example.compose.outlineLight
import com.example.compose.primaryDark
import com.example.compose.primaryLight
import com.example.compose.textLight
import pt.iade.games.iaderadio.MainActivity
import pt.iade.games.iaderadio.models.ScanFrequencyViewModel
import pt.iade.games.iaderadio.services.FileHelper
import pt.iade.games.iaderadio.ui.components.LockButton
import pt.iade.games.iaderadio.ui.components.frequency.AudioCirlce
import pt.iade.games.iaderadio.ui.components.frequency.AudioLine
import pt.iade.games.iaderadio.ui.components.frequency.ScanFrequency
import pt.iade.games.iaderadio.ui.components.shared.IconButton

class FrequencyActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val gameCode = FileHelper.readGameCodeFromFile(this).toString()
        setContent {
            AppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    FrequencyScreen (
                        code = gameCode,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun FrequencyScreen(modifier: Modifier = Modifier, code: String) {
    val context = LocalContext.current
    val viewModel = ScanFrequencyViewModel(context)
    var isLocked by remember { mutableStateOf(false) }
    AppTheme {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 8.dp), // Reduced vertical padding
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    icon = Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = "Go Back",
                    onClick = {
                        val intent = Intent(context, MainActivity::class.java)
                        context.startActivity(intent)
                    }
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "GAME CODE: $code",
                    color = textLight,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.weight(3f))
            }
            ScanFrequency(
                viewModel = viewModel,
                modifier = Modifier
                    .padding(top = 100.dp),
                locked = isLocked
            )
            Box(
                modifier = Modifier
                    .padding(top = 70.dp),
                contentAlignment = Alignment.Center,
            ) {
                AudioCirlce(scale = 2f, modifier = Modifier.align(Alignment.Center))
                LockButton(
                    isLocked = isLocked,
                    onToggleLock = { isLocked = it },
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            Text(
                text = "Your Audio:",
                color = Color.White,
                textAlign = TextAlign.Center,
                style = androidx.compose.ui.text.TextStyle(
                    fontSize = 20.sp,
                ),
                modifier = Modifier.padding(top = 75.dp, bottom = 16.dp)
            )
            Box(
                modifier = Modifier
                    .border(
                        width = 5.dp,                         // Thickness of the border
                        color = outlineLight,                 // Color of the border
                        shape = RoundedCornerShape(16.dp)     // Rounded corner radius
                    ),
                contentAlignment = Alignment.Center,
            ) {
                AudioLine(
                    modifier = Modifier
                        .width(300.dp)
                        .height(70.dp)
                        .padding(horizontal = 16.dp),         // Add horizontal padding
                    waveLength = 500f,
                    waveHeight = 40f,
                    speed = 2f,
                    segments = 9
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FrequencyActivityPreview() {
    FrequencyScreen(code = "1234")
}