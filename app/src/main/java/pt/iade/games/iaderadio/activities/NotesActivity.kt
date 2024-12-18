package pt.iade.games.iaderadio.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.compose.AppTheme
import com.example.compose.primaryDark
import com.example.compose.surfaceContainerDark
import com.example.compose.textLight
import pt.iade.games.iaderadio.MainActivity
import pt.iade.games.iaderadio.services.fileService.FileHelper
import pt.iade.games.iaderadio.services.fileService.Files
import pt.iade.games.iaderadio.ui.components.shared.IconButton

class NotesActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val gameCode = FileHelper.readTextFromFile(this, Files.GAME_CODE).toString()
        setContent {
            AppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NotesScreen(
                        code = gameCode,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}


@Composable
fun NotesScreen(modifier: Modifier = Modifier, code: String) {
    val context = LocalContext.current
    val initialNote = FileHelper.readTextFromFile(context, Files.NOTES) ?: ""
    val noteText = remember { mutableStateOf(TextFieldValue(initialNote)) }
    val scrollState = rememberScrollState()

    val saveNotes = { text: String ->
        FileHelper.writeTextToFile(context, text, Files.NOTES)
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
                modifier = Modifier.fillMaxWidth(),
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

            Text(
                text = "Notes:",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = primaryDark,
                modifier = Modifier.padding(bottom = 16.dp, top = 16.dp)
            )


            OutlinedTextField(
                value = noteText.value,
                onValueChange = {
                    noteText.value = it
                    saveNotes(it.text) // Save notes whenever the text changes
                },
                label = { Text("Write notes") },
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight() // Make it stretch to the full height
                    .background(surfaceContainerDark)
            )

            }

    }

}

@Preview(showBackground = true)
@Composable
fun NotesScreenPreview() {
    NotesScreen(code = "1234")
}
