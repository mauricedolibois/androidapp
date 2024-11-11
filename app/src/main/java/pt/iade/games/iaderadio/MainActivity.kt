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
import pt.iade.games.iaderadio.services.fileService.FileHelper
import pt.iade.games.iaderadio.services.fileService.Files

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                    StartApp(this@MainActivity)
                }
            }
        }
    }

fun StartApp(context: Context) {

        val gameCode = FileHelper.readTextFromFile(context, Files.GAME_CODE)
        val intent = if (gameCode != null) {
            Intent(context, MenuActivity::class.java)
        } else {
            Intent(context, CodeActivity::class.java)
        }
        context.startActivity(intent)
}
