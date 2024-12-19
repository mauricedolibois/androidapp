package pt.iade.games.iaderadio.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.compose.AppTheme
import com.example.compose.textLight
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import pt.iade.games.iaderadio.R
import pt.iade.games.iaderadio.network.FuelClient
import pt.iade.games.iaderadio.services.fileService.FileHelper
import pt.iade.games.iaderadio.services.fileService.Files
import pt.iade.games.iaderadio.ui.components.shared.IconButton
import pt.iade.games.iaderadio.ui.components.menu.MenuButton
import kotlin.math.abs

class MenuActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val gameCode = FileHelper.readTextFromFile(this, Files.GAME_CODE).toString()
        setContent {
            AppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MenuScreen(
                        code = gameCode,
                        modifier = Modifier.padding(innerPadding),
                    )
                }
            }
        }
    }
}

@Composable
fun MenuScreen(modifier: Modifier = Modifier, code: String) {
    val context = LocalContext.current
    val currentRoom = remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            while (true) {
                val sharedPref =
                    context.getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
                val sessionId = sharedPref.getInt("sessionId", -1)

                FuelClient.getCurrentRoombySessionID(context, sessionId) { room, roomError ->
                    currentRoom.value = room?.roomName.toString()
                    Log.d("MenuActivity", "Room: $room")
                }
                delay(2000L) // Fetch every 2 seconds
            }
        }
    }
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
                        FileHelper.deleteTextFile(context, Files.GAME_CODE)
                        FileHelper.deleteTextFile(context, Files.NOTES)
                        context.startActivity(intent)
                    }
                )
            }
            MenuButton("Scan\nFrequency",modifier = Modifier.height(125.dp).width(335.dp), onClick = {
                val intent = Intent(context, FrequencyActivity::class.java)
                context.startActivity(intent)
            }, fontSize = 30)
            Text(
                text = "Current Room: ${currentRoom.value}",
                color = textLight,
                modifier = Modifier.padding(vertical = 50.dp)
            )
            MenuButton("Notes",modifier = Modifier.height(70.dp).width(200.dp),color = textLight, onClick = {
                val intent = Intent(context, NotesActivity::class.java)
                context.startActivity(intent)
            })
        }
    }
}


@Preview(showBackground = false)
@Composable
fun MenuScreenPreview() {
    AppTheme {
        Column {
            MenuScreen(code = "123456")
        }
    }
}
