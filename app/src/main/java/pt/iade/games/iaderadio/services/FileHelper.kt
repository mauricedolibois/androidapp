package pt.iade.games.iaderadio.services

import android.content.Context
import java.io.File
import java.io.FileOutputStream

object FileHelper {

    private const val FILENAME = "game_code.txt"

    /**
     * Write the game code to a file.
     */
    fun writeGameCodeToFile(context: Context, gameCode: String) {
        try {
            val fileOutputStream: FileOutputStream = context.openFileOutput(FILENAME, Context.MODE_PRIVATE)
            fileOutputStream.write(gameCode.toByteArray())
            fileOutputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Read the game code from the file.
     */
    fun readGameCodeFromFile(context: Context): String? {
        return try {
            val file = File(context.filesDir, FILENAME)
            file.readText()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Delete the game code file.
     */
    fun deleteGameCodeFile(context: Context) {
        try {
            val file = File(context.filesDir, FILENAME)
            if (file.exists()) {
                file.delete()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
