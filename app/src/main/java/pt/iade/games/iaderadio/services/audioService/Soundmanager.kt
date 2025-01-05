package pt.iade.games.iaderadio.services.audioService

import android.content.Context
import android.media.MediaPlayer
import android.media.audiofx.Visualizer
import android.util.Log
import pt.iade.games.iaderadio.R
import java.util.Locale
import kotlin.math.sqrt

class SoundManager(private val context: Context) {

    private var mediaPlayer: MediaPlayer? = null
    private var currentSoundId: String? = null
    private var isRadioEffectPlaying: Boolean = false
    private var visualizer: Visualizer? = null
    private var currentAmplitude: Int = 0
    private var lastSoundPlayed: String? = null

    private val radioEffectFile = context.resources.openRawResourceFd(R.raw.radioeffect)

    // Play a sound by RoomName
    fun playSoundById(soundId: String) {
        val normalizedSoundId = soundId.toLowerCase(Locale.ROOT).replace(" ", "_")

        // Check if the requested sound is already playing
        if (currentSoundId == normalizedSoundId || lastSoundPlayed == normalizedSoundId ||
            ((currentSoundId=="shift_changed" || lastSoundPlayed=="shift_changed") && normalizedSoundId=="outside_area")||
            ((currentSoundId=="prison_opened"|| lastSoundPlayed=="prison_opened") && normalizedSoundId=="prison")) {
            Log.d("SoundManager", "Sound $normalizedSoundId is already playing.")
            return
        }

        stopCurrentSound()

        val soundResId = context.resources.getIdentifier(normalizedSoundId, "raw", context.packageName)

        if (soundResId == 0) {
            Log.e("SoundManager", "Sound resource $normalizedSoundId not found.")
            return
        }

        try {
            mediaPlayer = MediaPlayer.create(context, soundResId).apply {
                setOnCompletionListener {
                    Log.d("SoundManager", "Sound $normalizedSoundId completed. Resuming radio effect.")
                    currentSoundId = null
                    lastSoundPlayed=normalizedSoundId
                    playRadioEffect() // Resume the radio effect after sound finishes
                }
                setOnErrorListener { _, what, extra ->
                    Log.e("SoundManager", "Error playing sound $normalizedSoundId: what=$what, extra=$extra")
                    releaseResources()
                    true
                }
                start()
            }
            currentSoundId = normalizedSoundId
            setupVisualizer()
        } catch (e: Exception) {
            playRadioEffect()
            Log.e("SoundManager", "Error loading sound $normalizedSoundId: ${e.message}")
            e.printStackTrace()
        }
    }

    // Play the looping radio effect
    fun playRadioEffect() {
        if (isRadioEffectPlaying) {
            Log.d("SoundManager", "Radio effect is already playing.")
            return
        }

        stopCurrentSound()

        try {
            mediaPlayer = MediaPlayer().apply {
                setDataSource(radioEffectFile)
                isLooping = true
                setOnPreparedListener {
                    Log.d("SoundManager", "Playing radio effect.")
                    start()
                }
                setOnErrorListener { _, what, extra ->
                    Log.e("SoundManager", "Error playing radio effect: what=$what, extra=$extra")
                    releaseResources()
                    true
                }
                prepare()
            }
            isRadioEffectPlaying = true
            setupVisualizer()
        } catch (e: Exception) {
            Log.e("SoundManager", "Error loading radio effect: ${e.message}")
            e.printStackTrace()
        }
    }

    // Stop the current sound
    fun stopCurrentSound() {
        mediaPlayer?.apply {
            stop()
            release()
        }
        releaseVisualizer()
        mediaPlayer = null
        currentSoundId = null
        isRadioEffectPlaying = false
    }

    // Set up a Visualizer for the MediaPlayer
    private fun setupVisualizer() {
        mediaPlayer?.let { player ->
            val sessionId = player.audioSessionId
            visualizer = Visualizer(sessionId).apply {
                captureSize = Visualizer.getCaptureSizeRange()[1] // Max capture size
                setDataCaptureListener(object : Visualizer.OnDataCaptureListener {
                    override fun onWaveFormDataCapture(
                        visualizer: Visualizer?,
                        waveform: ByteArray?,
                        samplingRate: Int
                    ) {
                        if (waveform != null) {
                            currentAmplitude = calculateAmplitude(waveform)
                        }
                    }

                    override fun onFftDataCapture(
                        visualizer: Visualizer?,
                        fft: ByteArray?,
                        samplingRate: Int
                    ) {
                        // FFT data not needed here
                    }
                }, Visualizer.getMaxCaptureRate(), true, false)
                enabled = true
            }
        }
    }

    // Calculate amplitude from waveform data
    private fun calculateAmplitude(waveform: ByteArray): Int {
        return sqrt(waveform.map { it.toInt() * it.toInt() }.average()).toInt()
    }

    // Get the current real-time amplitude of the audio source
    fun getCurrentAmplitude(): Int {
        return if (isRadioEffectPlaying) 0 else currentAmplitude / 100
    }

    // Release the visualizer resources
    private fun releaseVisualizer() {
        visualizer?.release()
        visualizer = null
    }

    private fun releaseResources() {
        stopCurrentSound()
        releaseVisualizer()
    }
}
