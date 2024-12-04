package pt.iade.games.iaderadio.services.audioService

import android.annotation.SuppressLint
import android.content.Context
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Environment
import android.util.Log
import java.io.File
import kotlin.math.max
import kotlin.math.min

class AudioRecorder(private val context: Context) {

    private var outputFilePath: String = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).absolutePath + "/recording.mp3"
    private var mediaRecorder: MediaRecorder? = null
    private var isRecording: Boolean = false

    init {
        initializeRecorder()
    }

    @SuppressLint("NewApi")
    private fun initializeRecorder() {
        try {
            mediaRecorder = MediaRecorder(context).apply { // Pass context to the MediaRecorder
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                setOutputFile(outputFilePath)
            }
        } catch (e: Exception) {
            Log.e("AudioRecorder", "Error initializing recorder: ${e.message}")
            e.printStackTrace()
        }
    }

    fun startRecording() {
        try {
            if (!isRecording) {
                mediaRecorder?.prepare()
                mediaRecorder?.start()
                isRecording = true
                Log.d("AudioRecorder", "Recording started.")
            }
        } catch (e: Exception) {
            Log.e("AudioRecorder", "Error starting recording: ${e.message}")
            e.printStackTrace()
        }
    }

    fun stopRecording() {
        if (isRecording) {
            try {
                mediaRecorder?.apply {
                    stop()
                    release()
                }
                mediaRecorder = null
                isRecording = false
                Log.d("AudioRecorder", "Recording stopped and saved at: $outputFilePath")

                // Verify file and play if valid
                val file = File(outputFilePath)
                if (file.exists() && file.length() > 0) {
                    Log.d("AudioRecorder", "File exists and is valid. Playing now.")
                    //playRecordedAudio(outputFilePath)
                } else {
                    Log.e("AudioRecorder", "Recording failed. File is invalid.")
                }

            } catch (e: Exception) {
                Log.e("AudioRecorder", "Error stopping recording: ${e.message}")
                e.printStackTrace()
            }
        }
    }

    private fun playRecordedAudio(filePath: String) {
        try {
            val mediaPlayer = MediaPlayer().apply {
                setDataSource(filePath)
                setOnPreparedListener {
                    Log.d("AudioRecorder", "Playing recorded audio.")
                    start()
                }
                setOnCompletionListener {
                    Log.d("AudioRecorder", "Playback complete. Releasing resources.")
                    release()
                }
                setOnErrorListener { mp, what, extra ->
                    Log.e("MediaPlayer", "Playback error: what=$what, extra=$extra")
                    mp.release()
                    true
                }
            }
            mediaPlayer.prepareAsync()
        } catch (e: Exception) {
            Log.e("AudioRecorder", "Error during playback: ${e.message}")
            e.printStackTrace()
        }
    }

    fun getCurrentMicrophoneVolume(): Int {
        return if (isRecording) {
            val maxAmplitude = mediaRecorder?.maxAmplitude ?: 0
            // Scale the amplitude to fit between 5 and 40
            val scaledValue = (maxAmplitude / 5000.0 * 35 + 5).toInt()
            // Clamp the value to ensure it's within bounds
            max(5, min(scaledValue, 40))
        } else {
            5 // Return the minimum value if not recording
        }
    }
}
