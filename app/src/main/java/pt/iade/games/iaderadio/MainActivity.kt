package pt.iade.games.iaderadio

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.compose.AppTheme
import pt.iade.games.iaderadio.activities.CodeActivity
import pt.iade.games.iaderadio.activities.MenuActivity
import pt.iade.games.iaderadio.network.FuelClient
import pt.iade.games.iaderadio.services.fileService.FileHelper
import pt.iade.games.iaderadio.services.fileService.Files
import android.util.Log


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                    startApp(this@MainActivity)
                }
            }
        }
    }

fun startApp(context: Context) {

        val gameCode = FileHelper.readTextFromFile(context, Files.GAME_CODE)
        val intent = if (gameCode != null) {
            FuelClient.getSessionByCode(context,gameCode) { session, error ->
                if (session != null) {
                    Log.d("MainActivity", "Session: $session")
                    FuelClient.getCurrentRoombySessionID(context,session.toInt()) { room, error ->
                        if (room != null) {
                            Log.d("MainActivity", "Room: $room")
                        } else {
                            Log.e("MainActivity", "Error fetching room: $error")                        }
                    }
                } else {
                    Log.e("MainActivity", "Error fetching session: $error")
                }
            }
            Intent(context, MenuActivity::class.java)
        } else {
            Intent(context, CodeActivity::class.java)
        }
        context.startActivity(intent)
}
