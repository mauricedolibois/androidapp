package pt.iade.games.iaderadio.services.audioService

import android.content.Context
import android.media.MediaPlayer
import android.media.audiofx.Visualizer
import android.util.Log
import kotlin.math.sqrt
import pt.iade.games.iaderadio.R

class SoundManager(private val context: Context) {

    private var mediaPlayer: MediaPlayer? = null
    private var currentSoundId: String? = null
    private var isRadioEffectPlaying: Boolean = false
    private var visualizer: Visualizer? = null
    private var currentAmplitude: Int = 0

    private val radioEffectFile = context.resources.openRawResourceFd(R.raw.radioeffect)

    // Play a sound by ID
    fun playSoundById(soundId: String) {
        if (currentSoundId == soundId) {
            Log.d("SoundManager", "Sound $soundId is already playing.")
            return
        }

        stopCurrentSound()

        val soundResId = context.resources.getIdentifier(soundId, "raw", context.packageName)

        if (soundResId == 0) {
            Log.e("SoundManager", "Sound resource $soundId not found.")
            return
        }

        try {
            mediaPlayer = MediaPlayer.create(context, soundResId).apply {
                setOnCompletionListener {
                    Log.d("SoundManager", "Sound $soundId completed. Resuming radio effect.")
                    currentSoundId = null
                    playRadioEffect() // Resume the radio effect after sound finishes
                }
                setOnErrorListener { _, what, extra ->
                    Log.e("SoundManager", "Error playing sound $soundId: what=$what, extra=$extra")
                    releaseResources()
                    true
                }
                start()
            }
            currentSoundId = soundId
            setupVisualizer()
        } catch (e: Exception) {
            playRadioEffect()
            Log.e("SoundManager", "Error loading sound $soundId: ${e.message}")
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
            if (isPlaying) stop()
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
        return if (isRadioEffectPlaying) 0 else currentAmplitude/100
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
