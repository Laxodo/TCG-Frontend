package tcg.frontend.infraestructura.repository

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders

class MediaRepository(private val url: String, private val _client: HttpClient) {
    suspend fun uploadImage(fileName: String, bytes: ByteArray): Result<String> {
        return runCatching {
            val response = _client.submitFormWithBinaryData(
                url = "$url/media/upload",
                formData = formData {
                    append("file", bytes, Headers.build {
                        append(
                            HttpHeaders.ContentDisposition,
                            "filename=$fileName"
                        )
                        append(
                            HttpHeaders.ContentType,
                            "image/png"
                        )
                    })
                }
            )

            if (response.status.value !in 200 ..299) {
                throw Exception("${response.status.value}-${response.status.description}")
            }

            response.body<String>()
        }
    }
}