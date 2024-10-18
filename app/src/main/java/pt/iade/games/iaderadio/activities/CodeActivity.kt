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
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.compose.AppTheme
import pt.iade.games.iaderadio.R
import pt.iade.games.iaderadio.services.FileHelper
import pt.iade.games.iaderadio.ui.components.InputField

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
                                FileHelper.writeGameCodeToFile(this, gameCode) // Using FileHelper                                startActivity(intent)
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

        var textState by remember { mutableStateOf(TextFieldValue("")) }

        InputField(
            placeholder = "ENTER GAME CODE",
            value = textState,
            onValueChange = { textState = it },
            onSubmit = { inputText ->
                Log.d("CodeScreen", "Submitted text: $inputText")
                onSubmit(inputText)
            }
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
