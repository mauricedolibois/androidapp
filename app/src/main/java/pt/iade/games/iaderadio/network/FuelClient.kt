package pt.iade.games.iaderadio.network

import android.R
import android.content.Context
import android.util.Log
import pt.iade.games.iaderadio.models.Room
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.gson.responseObject
import pt.iade.games.iaderadio.services.fileService.FileHelper
import pt.iade.games.iaderadio.services.fileService.Files

object FuelClient {

    // Base URL for the Node.js server
    private const val BASE_URL = "http://10.0.2.2:3000"

    // Function to fetch rooms from the server
    fun getRooms(onResult: (List<Room>?, String?) -> Unit) {
        Fuel.get("$BASE_URL/")
            .responseObject<List<Room>> { _, _, result ->
                result.fold(
                    success = { rooms ->
                        onResult(rooms, null)
                    },
                    failure = { error ->
                        Log.e("FuelClient", "Error fetching rooms: ${error.message}")
                        onResult(null, error.message)
                    }
                )
            }
    }

    fun getSessionByCode(context: Context, code: String, onResult: (Int?, String?) -> Unit) {
        if (code != null) {
            Fuel.get("$BASE_URL/game/checkCode/$code")
                .responseObject { _, _, result ->
                    result.fold(
                        success = { session ->
                            // Store sessionId in SharedPreferences
                            val sharedPref = context.getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
                            with(sharedPref.edit()) {
                                putInt("sessionId", session)
                                apply()
                            }
                            onResult(session, null)
                        },
                        failure = { error ->
                            Log.e("FuelClient", "Error fetching session: ${error.message}")
                            onResult(null, error.message)
                        }
                    )
                }
        }
    }

    fun getCurrentRoomIDbySessionID(context: Context, sessionID: Int, onResult: (String?, String?) -> Unit) {
        Fuel.get("$BASE_URL/room/getCurrentRoom/$sessionID")
            .responseString { _, _, result ->
                result.fold(
                    success = { room ->
                        // Store roomId in SharedPreferences
                        val sharedPref = context.getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
                        with(sharedPref.edit()) {
                            putString("roomId", room)
                            apply()
                        }
                        onResult(room, null)
                    },
                    failure = { error ->
                        Log.e("FuelClient", "Error fetching room: ${error.message}")
                        onResult(null, error.message)
                    }
                )
            }
    }

    fun getFrequencBySessionIDAndRoomId(sessionID: Int, roomId: String, onResult: (String?, String?) -> Unit) {
        Fuel.get("$BASE_URL/frequency/getFrequency/$sessionID/$roomId")
            .responseString { _, _, result ->
                result.fold(
                    success = { frequency ->
                        // Store frequency in SharedPreferences
                        onResult(frequency, null)
                    },
                    failure = { error ->
                        Log.e("FuelClient", "Error fetching frequency: ${error.message}")
                        onResult(null, error.message)
                    }
                )
            }

    }

}
