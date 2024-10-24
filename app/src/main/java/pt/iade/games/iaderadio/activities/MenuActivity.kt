package pt.iade.games.iaderadio.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Logout
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.compose.AppTheme
import com.example.compose.textLight
import pt.iade.games.iaderadio.R
import pt.iade.games.iaderadio.services.FileHelper
import pt.iade.games.iaderadio.ui.components.IconButton
import pt.iade.games.iaderadio.ui.components.MenuButton

class MenuActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val gameCode = FileHelper.readGameCodeFromFile(this).toString()
        setContent {
            AppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MenuScreen(
                        code = gameCode,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun MenuScreen(modifier: Modifier = Modifier, code: String) {
    val context = LocalContext.current
    AppTheme {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.iade_radio_logo),
                contentDescription = "IADE Radio Logo",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            )

            Row(
                modifier = Modifier
                    .width(250.dp).padding(bottom = 50.dp),
                horizontalArrangement = Arrangement.Absolute.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "GAME CODE: $code",
                    color = textLight,
                    modifier = Modifier.weight(1f)
                )

                IconButton(
                    icon = Icons.AutoMirrored.Rounded.Logout,
                    contentDescription = "Logout",
                    onClick = {
                        val intent = Intent(context, CodeActivity::class.java)
                        FileHelper.deleteGameCodeFile(context)
                        context.startActivity(intent)
                    }
                )
            }
            MenuButton("Scan\nFrequency",125, onClick = {
                val intent = Intent(context, FrequencyActivity::class.java)
                context.startActivity(intent)
            },27)
            MenuButton("Notes",55, onClick = {})
        }
    }
}


@Preview(showBackground = true)
@Composable
fun MenuScreenPreview() {
    AppTheme {
        Column {
            MenuScreen(code = "123456")
        }
    }
}
