package tcg.frontend.di

import com.russhwolf.settings.Settings
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import tcg.frontend.aplicacion.UserSessionManager
import tcg.frontend.aplicacion.expansion.listar.ListExpansionUseCase
import tcg.frontend.aplicacion.generation.listar.ListGenerationUseCase
import tcg.frontend.aplicacion.usuarios.delete.DeleteUserUseCase
import tcg.frontend.aplicacion.login.LoginUseCase
import tcg.frontend.aplicacion.market.openboosted.OpenBoosterUseCase
import tcg.frontend.aplicacion.market.quicksell.QuickSellUseCase
import tcg.frontend.aplicacion.usercard.listCollection.ListUserCardCollectionUseCase
import tcg.frontend.aplicacion.usercard.listar.ListUserCardUseCase
import tcg.frontend.aplicacion.usuarios.getuser.GetUserUseCase
import tcg.frontend.aplicacion.usuarios.listar.ListUsersUseCase
import tcg.frontend.dominio.IExpansionRepository
import tcg.frontend.dominio.IGenerationRepository
import tcg.frontend.dominio.IMarketRepository
import tcg.frontend.dominio.IUserRepository
import tcg.frontend.dominio.User
import tcg.frontend.infraestructura.TokenStorage
import tcg.frontend.infraestructura.ktor.createHttpClient
import tcg.frontend.infraestructura.repository.ExpansionRepository
import tcg.frontend.infraestructura.repository.GenerationRepository
import tcg.frontend.infraestructura.repository.MarketRepository
import tcg.frontend.infraestructura.repository.UserRepository
import tcg.frontend.ui.MainViewModel
import tcg.frontend.ui.administracion.AdminMainViewModel
import tcg.frontend.ui.administracion.users.UserViewModel
import tcg.frontend.ui.administracion.users.form.UserFormViewModel
import tcg.frontend.ui.login.LoginViewModel
import tcg.frontend.ui.usuario.UserMainViewModel
import tcg.frontend.ui.usuario.expansion.ExpansionViewModel
import tcg.frontend.ui.administracion.generation.GenerationViewModel
import tcg.frontend.ui.usuario.market.openbooster.OpenBoosterViewModel
import tcg.frontend.ui.usuario.usercard.collectionView.UserCardCollectionViewModel
import tcg.frontend.ui.usuario.usercard.galleryView.UserCardGalleryViewModel
import tcg.frontend.ui.usuario.usercard.galleryView.view.UserCardGalleryDetailViewModel

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
    single<IExpansionRepository> { ExpansionRepository("http://192.168.0.113:8000", get()) }
    single<IGenerationRepository> { GenerationRepository("http://192.168.0.113:8000", get()) }
    single<IMarketRepository> { MarketRepository("http://192.168.0.113:8000", get()) }
    single { UserMainViewModel(get(), get()) }
    single { createHttpClient(get()) }

    factory { LoginUseCase(get(), get()) }
    factory { ListUsersUseCase(get()) }
    factory { DeleteUserUseCase(get()) }
    factory { ListExpansionUseCase(get()) }
    factory { ListGenerationUseCase(get()) }
    factory { ListUserCardUseCase(get()) }
    factory { ListUserCardCollectionUseCase(get()) }
    factory { QuickSellUseCase(get()) }
    factory { OpenBoosterUseCase(get()) }
    factory { GetUserUseCase(get()) }

    viewModel { AdminMainViewModel() }
    viewModel { (item: User?) -> UserFormViewModel(item = item) }
    viewModel { UserViewModel(get(), get()) }
    viewModel { LoginViewModel(get()) }
    viewModel { MainViewModel(get()) }
    viewModel { ExpansionViewModel(get()) }
    viewModel { GenerationViewModel(get()) }
    viewModel { OpenBoosterViewModel(get(), get()) }
    viewModel { (id: Int, idExpansion: Int) -> UserCardGalleryViewModel(idUser = id, idExpansion = idExpansion, listUserCardUseCase = get(), quickSellUseCase = get(), userMainViewModel = get()) }
    viewModel { (id: Int, idExpansion: Int) -> UserCardCollectionViewModel(
        idUser = id, idExpansion = idExpansion,
        listUserCardCollectionUseCase = get()
    ) }
    viewModel { UserCardGalleryDetailViewModel(get(), get(), null) }
}