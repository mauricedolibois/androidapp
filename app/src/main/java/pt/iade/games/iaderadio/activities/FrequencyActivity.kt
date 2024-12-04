package pt.iade.games.iaderadio.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.compose.AppTheme
import com.example.compose.outlineLight
import com.example.compose.textLight
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import pt.iade.games.iaderadio.MainActivity
import pt.iade.games.iaderadio.models.ScanFrequencyViewModel
import pt.iade.games.iaderadio.services.audioService.AudioRecorder
import pt.iade.games.iaderadio.services.fileService.FileHelper
import pt.iade.games.iaderadio.services.fileService.Files
import pt.iade.games.iaderadio.ui.components.LockButton
import pt.iade.games.iaderadio.ui.components.frequency.AudioCirlce
import pt.iade.games.iaderadio.ui.components.frequency.AudioLine
import pt.iade.games.iaderadio.ui.components.frequency.ScanFrequency
import pt.iade.games.iaderadio.ui.components.shared.IconButton

class FrequencyActivity : ComponentActivity() {

    private lateinit var audioRecorder: AudioRecorder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Initialize AudioRecorder
        audioRecorder = AudioRecorder(this)

        // Check permissions
        if (!hasRequiredPermissions()) {
            requestPermissions()
        } else {
            audioRecorder.startRecording()
        }

        // Read game code
        val gameCode = FileHelper.readTextFromFile(this, Files.GAME_CODE).toString()

        setContent {
            AppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    FrequencyScreen(
                        code = gameCode,
                        audioRecorder = audioRecorder,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }


    }

    private fun hasRequiredPermissions(): Boolean {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissions() {
        val permissions = arrayOf(
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        ActivityCompat.requestPermissions(this, permissions, 0)
        audioRecorder.startRecording()
    }

    override fun onDestroy() {
        super.onDestroy()
        audioRecorder.stopRecording()
    }

}


@Composable
fun FrequencyScreen(modifier: Modifier = Modifier, code: String, audioRecorder: AudioRecorder) {
    val context = LocalContext.current
    val viewModel = ScanFrequencyViewModel(context)
    var isLocked by remember { mutableStateOf(false) }
    var waveHeight by remember { mutableStateOf(40f) } // Initial wave height
    val coroutineScope = rememberCoroutineScope()

    // Start observing microphone volume
    LaunchedEffect(audioRecorder) {
        coroutineScope.launch {
            while (true) {
                waveHeight = audioRecorder.getCurrentMicrophoneVolume().toFloat()
                delay(100L) // Refresh every 100ms
            }
        }
    }

    AppTheme {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 8.dp),
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
                        audioRecorder.stopRecording()
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
                modifier = Modifier.padding(top = 100.dp),
                locked = isLocked
            )
            Box(
                modifier = Modifier.padding(top = 70.dp),
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
                style = androidx.compose.ui.text.TextStyle(fontSize = 20.sp),
                modifier = Modifier.padding(top = 75.dp, bottom = 16.dp)
            )
            Box(
                modifier = Modifier
                    .border(
                        width = 5.dp,
                        color = outlineLight,
                        shape = RoundedCornerShape(16.dp)
                    ),
                contentAlignment = Alignment.Center,
            ) {
                AudioLine(
                    modifier = Modifier
                        .width(300.dp)
                        .height(70.dp)
                        .padding(horizontal = 16.dp),
                    waveLength = 500f,
                    waveHeight = waveHeight, // Use dynamic waveHeight
                    speed = 2f,
                    segments = 9
                )

            }
            Text(
                modifier = Modifier.padding(top = 16.dp),
                text = " speechtotexttext",
                color = textLight,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FrequencyActivityPreview() {
}
