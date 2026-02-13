package tcg.frontend.di

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.http.headers
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import tcg.frontend.aplicacion.login.LoginUseCase
import tcg.frontend.dominio.IUserRepository
import tcg.frontend.infraestructura.repository.UserRepository
import tcg.frontend.ui.MainViewModel
import tcg.frontend.ui.login.LoginViewModel

val appModel = module{

    single {
        HttpClient() {
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                })
            }
        }
    }

    single<IUserRepository> { UserRepository("http://localhost:8000", get()) }

    factory { LoginUseCase(get()) }

    viewModel { LoginViewModel(get()) }
    viewModel { MainViewModel() }
}