package tcg.frontend.ui.administracion.media

import androidx.lifecycle.ViewModel
import tcg.frontend.infraestructura.repository.MediaRepository

class MediaViewModel(private val mediaRepository: MediaRepository): ViewModel() {
    suspend fun uploadImage(fileName: String, bytes: ByteArray): Result<String> {
        return mediaRepository.uploadImage(fileName, bytes)
    }
}