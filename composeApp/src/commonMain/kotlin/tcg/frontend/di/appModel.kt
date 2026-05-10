package tcg.frontend.di

import com.russhwolf.settings.Settings
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.http.headers
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import tcg.frontend.aplicacion.UserSessionManager
import tcg.frontend.aplicacion.delete.DeleteUserUseCase
import tcg.frontend.aplicacion.login.LoginUseCase
import tcg.frontend.aplicacion.usuarios.listar.ListUsersUseCase
import tcg.frontend.dominio.IUserRepository
import tcg.frontend.dominio.User
import tcg.frontend.infraestructura.TokenStorage
import tcg.frontend.infraestructura.ktor.createHttpClient
import tcg.frontend.infraestructura.repository.UserRepository
import tcg.frontend.ui.MainViewModel
import tcg.frontend.ui.administracion.AdminMainViewModel
import tcg.frontend.ui.administracion.users.UserViewModel
import tcg.frontend.ui.administracion.users.form.UserFormViewModel
import tcg.frontend.ui.login.LoginViewModel
import tcg.frontend.ui.usuario.UserMainViewModel

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

    single { TokenStorage(Settings()) }
    single { UserSessionManager(get()) }

    single<IUserRepository> { UserRepository("http://192.168.0.113:8000", get()) }
    single { createHttpClient(get()) }

    factory { LoginUseCase(get(), get()) }
    factory { ListUsersUseCase(get()) }
    factory { DeleteUserUseCase(get()) }

    viewModel { AdminMainViewModel() }
    viewModel { UserMainViewModel() }
    viewModel { (item: User?) -> UserFormViewModel(item = item) }
    viewModel { UserViewModel(get(), get()) }
    viewModel { LoginViewModel(get()) }
    viewModel { MainViewModel(get()) }
}