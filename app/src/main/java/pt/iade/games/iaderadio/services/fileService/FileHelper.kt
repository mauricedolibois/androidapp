package pt.iade.games.iaderadio.services.fileService

import android.content.Context
import java.io.File
import java.io.FileOutputStream

object FileHelper {

    /**
     * Write the game code to a file.
     */
    fun writeTextToFile(context: Context, text: String, filename: Files) {
        try {
            val fileOutputStream: FileOutputStream = context.openFileOutput(filename.fileName, Context.MODE_PRIVATE)
            fileOutputStream.write(text.toByteArray())
            fileOutputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Read the game code from the file.
     */
    fun readTextFromFile(context: Context, filename: Files): String? {
        return try {
            val file = File(context.filesDir, filename.fileName)
            file.readText()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Delete the game code file.
     */
    fun deleteTextFile(context: Context, filename: Files) {
        try {
            val file = File(context.filesDir, filename.fileName)
            if (file.exists()) {
                file.delete()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
