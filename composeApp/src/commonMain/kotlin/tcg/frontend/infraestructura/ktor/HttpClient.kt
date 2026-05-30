package tcg.frontend.infraestructura.ktor

import io.ktor.client.HttpClient
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.header
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import tcg.frontend.infraestructura.TokenStorage

fun createHttpClient(tokenStorage: TokenStorage): HttpClient {
    return HttpClient {
        install(DefaultRequest) {
            header(HttpHeaders.ContentType, ContentType.Application.Json)
        }
        //logs
        install(Logging) {

            logger = object : Logger {
                override fun log(message: String) {
                    println("KTOR CLIENT LOG: $message")
                }
            }
            level = LogLevel.ALL
        }
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
                isLenient = true
            })
        }
        install(Auth) {
            bearer {
                sendWithoutRequest {
                    true
                }
                loadTokens {
                    val accessToken = tokenStorage.getAccessToken()

                    if (accessToken != null) {
                        BearerTokens(accessToken, "")
                    } else {
                        null
                    }
                }
            }
        }

        install(HttpTimeout) {
            requestTimeoutMillis = 15000
        }
    }
}