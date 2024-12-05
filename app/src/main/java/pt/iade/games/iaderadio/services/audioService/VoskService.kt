package pt.iade.games.iaderadio.services.audioService

import android.content.Context
import android.util.Log
import org.vosk.Model
import org.vosk.Recognizer
import org.vosk.android.RecognitionListener
import org.vosk.android.SpeechService
import org.vosk.android.StorageService

class VoskService(private val context: Context) : RecognitionListener {

    private var model: Model? = null
    private var speechService: SpeechService? = null

    // Callbacks for speech recognition results and errors
    var onResult: ((String) -> Unit)? = null
    var onError: ((String) -> Unit)? = null

    /**
     * Initializes the Vosk model asynchronously.
     *
     * @param onModelLoaded Called when the model is successfully loaded.
     * @param onError Called when an error occurs during model initialization.
     */
    fun initializeModel(onModelLoaded: () -> Unit, onError: (Exception) -> Unit) {
        StorageService.unpack(context, "model-en-us", "model",
            { loadedModel ->
                model = loadedModel
                onModelLoaded()
                Log.d("VoskService", "Model successfully loaded")
            },
            { exception ->
                Log.e("VoskService", "Failed to load model: ${exception.message}")
                onError(exception)
            }
        )
    }

    /**
     * Starts listening to the microphone input.
     */
    fun startListening() {
        try {
            if (model != null) {
                val recognizer = Recognizer(model, 16000.0f)
                speechService = SpeechService(recognizer, 16000.0f)
                speechService?.startListening(this)
                Log.d("VoskService", "Started listening")
            } else {
                Log.e("VoskService", "Model is not initialized. Call initializeModel first.")
                onError?.invoke("Model is not initialized.")
            }
        } catch (e: Exception) {
            Log.e("VoskService", "Error starting listening: ${e.message}")
            onError?.invoke(e.message ?: "Unknown error")
        }
    }

    /**
     * Stops listening to the microphone input.
     */
    fun stopListening() {
        speechService?.stop()
        Log.d("VoskService", "Stopped listening")
    }

    /**
     * Releases resources and shuts down the service.
     */
    fun shutdown() {
        speechService?.shutdown()
        model?.close()
        speechService = null
        model = null
        Log.d("VoskService", "Service shut down")
    }

    override fun onPartialResult(hypothesis: String?) {
        onResult?.invoke(hypothesis ?: "")
    }

    override fun onResult(hypothesis: String?) {
        onResult?.invoke(hypothesis ?: "")
    }

    override fun onFinalResult(hypothesis: String?) {
        onResult?.invoke(hypothesis ?: "")
    }

    override fun onError(exception: Exception?) {
        onError?.invoke(exception?.message ?: "Unknown error")
    }

    override fun onTimeout() {
        onError?.invoke("Timeout occurred")
    }
}
