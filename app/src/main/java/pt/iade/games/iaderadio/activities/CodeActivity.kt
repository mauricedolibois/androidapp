package pt.iade.games.iaderadio.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.compose.AppTheme
import pt.iade.games.iaderadio.R
import pt.iade.games.iaderadio.services.fileService.FileHelper
import pt.iade.games.iaderadio.services.fileService.Files
import pt.iade.games.iaderadio.ui.components.shared.InputField

class CodeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CodeScreen(
                        modifier = Modifier.padding(innerPadding),
                        onSubmit = { gameCode ->
                            if (gameCode.isNotEmpty()) {
                                // Log the navigation attempt
                                Log.d("CodeActivity", "Navigating to MenuActivity with code: $gameCode")
                                val intent = Intent(this, MenuActivity::class.java)
                                intent.putExtra("GAME_CODE", gameCode)
                                FileHelper.writeTextToFile(this, gameCode, Files.GAME_CODE) // Using FileHelper                                startActivity(intent)
                                startActivity(intent)
                            } else {
                                // Log if the code is empty
                                Log.d("CodeActivity", "Game code is empty. Cannot navigate.")
                            }
                        }
                    )
                }
            }
        }
    }
}





@Composable
fun CodeScreen(modifier: Modifier = Modifier, onSubmit: (String) -> Unit) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Image(
            painter = painterResource(id = R.drawable.iade_radio_logo),
            contentDescription = "IADE Radio Logo",
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
        )


        Text(
            text = "Connect Companion App:",
            fontSize = 23.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 50.dp, top = 50.dp)
        )

        var textState by remember { mutableStateOf(TextFieldValue("")) }

        InputField(
            placeholder = "ENTER GAME CODE",
            value = textState,
            onValueChange = { newValue ->
                if (newValue.text.matches(Regex("^[a-zA-Z\\d]*$")) && newValue.text.length <= 5) { // filtering letters and numbers, max 5 characters
                    textState = newValue.copy(text = newValue.text.uppercase())
                }
            },
            onSubmit = { inputText ->
                Log.d("CodeScreen", "Submitted text: $inputText")
                onSubmit(inputText)
            },
            modifier = Modifier
                .width(200.dp)
                .align(Alignment.CenterHorizontally)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CodeScreenPreview() {
    AppTheme {
        CodeScreen(onSubmit = {})
    }
}
