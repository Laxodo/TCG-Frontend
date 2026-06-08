package tcg.frontend.di

import com.russhwolf.settings.Settings
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import tcg.frontend.aplicacion.UserSessionManager
import tcg.frontend.aplicacion.expansion.crear.CreateExpansionUseCase
import tcg.frontend.aplicacion.generation.listExpansionGeneration.ListExpansionGenerationUseCase
import tcg.frontend.aplicacion.generation.crear.CreateGenerationUseCase
import tcg.frontend.aplicacion.generation.listar.ListGenerationUseCase
import tcg.frontend.aplicacion.usuarios.delete.DeleteUserUseCase
import tcg.frontend.aplicacion.login.LoginUseCase
import tcg.frontend.aplicacion.market.openboosted.OpenBoosterUseCase
import tcg.frontend.aplicacion.market.quicksell.QuickSellUseCase
import tcg.frontend.aplicacion.usercard.listCollection.ListUserCardCollectionUseCase
import tcg.frontend.aplicacion.usercard.listar.ListUserCardUseCase
import tcg.frontend.aplicacion.usuarios.getuser.GetUserUseCase
import tcg.frontend.aplicacion.usuarios.listar.ListUsersUseCase
import tcg.frontend.application.register.RegisterUserCase
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
import tcg.frontend.ui.register.RegisterViewModel
import tcg.frontend.ui.administracion.AdminMainViewModel
import tcg.frontend.ui.administracion.users.UserViewModel
import tcg.frontend.ui.usuario.expansion.ExpansionViewModel as UserExpansionViewModel
import tcg.frontend.ui.administracion.expansion.ExpansionViewModel as AdminExpansionViewModel
import tcg.frontend.ui.administracion.users.form.UserFormViewModel
import tcg.frontend.ui.login.LoginViewModel
import tcg.frontend.ui.usuario.UserMainViewModel
import tcg.frontend.ui.administracion.generation.GenerationViewModel
import tcg.frontend.ui.usuario.market.openbooster.OpenBoosterViewModel
import tcg.frontend.ui.usuario.usercard.collectionView.UserCardCollectionViewModel
import tcg.frontend.ui.usuario.usercard.galleryView.UserCardGalleryViewModel
import tcg.frontend.ui.usuario.usercard.galleryView.view.UserCardGalleryDetailViewModel
import tcg.frontend.ui.administracion.card.CardViewModel
import tcg.frontend.aplicacion.card.create.CreateCardUseCase
import tcg.frontend.aplicacion.card.createBatch.CreateCardsBatchUseCase
import tcg.frontend.aplicacion.expansion.listCardExpansion.ListCardExpansionUseCase
import tcg.frontend.dominio.ICardRepository
import tcg.frontend.infraestructura.repository.CardRepository
import tcg.frontend.infraestructura.repository.MediaRepository
import tcg.frontend.ui.administracion.media.MediaViewModel

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

    single<IUserRepository> { UserRepository("http://127.0.0.1:8000", get()) }
    single<IExpansionRepository> { ExpansionRepository("http://127.0.0.1:8000", get()) }
    single<IGenerationRepository> { GenerationRepository("http://127.0.0.1:8000", get()) }
    single<IMarketRepository> { MarketRepository("http://127.0.0.1:8000", get()) }
    single<ICardRepository> { CardRepository("http://127.0.0.1:8000", get()) }
    single { UserMainViewModel(get(), get()) }
    single { createHttpClient(get()) }
    single { MediaRepository("http://127.0.0.1:8000", get()) }

    factory { LoginUseCase(get(), get()) }
    factory { RegisterUserCase(get()) }
    factory { ListUsersUseCase(get()) }
    factory { DeleteUserUseCase(get()) }
    factory { ListExpansionGenerationUseCase(get()) }
    factory { CreateExpansionUseCase(get()) }
    factory { CreateGenerationUseCase(get()) }
    factory { ListGenerationUseCase(get()) }
    factory { ListUserCardUseCase(get()) }
    factory { ListUserCardCollectionUseCase(get()) }
    factory { QuickSellUseCase(get()) }
    factory { OpenBoosterUseCase(get()) }
    factory { GetUserUseCase(get()) }
    factory { ListCardExpansionUseCase(get()) }
    factory { CreateCardUseCase(get()) }
    factory { CreateCardsBatchUseCase(get()) }

    viewModel { AdminMainViewModel() }
    viewModel { (item: User?) -> UserFormViewModel(item = item) }
    viewModel { UserViewModel(get(), get()) }
    viewModel { LoginViewModel(get()) }
    viewModel { RegisterViewModel(get()) }
    viewModel { MainViewModel(get()) }
    viewModel { CardViewModel(get(), get(), get()) }
    viewModel { UserExpansionViewModel(get()) }
    viewModel { MediaViewModel(get()) }
    viewModel { AdminExpansionViewModel(get(), get()) }
    viewModel { GenerationViewModel(get(), get()) }
    viewModel { OpenBoosterViewModel(get(), get()) }
    viewModel { (id: Int, idExpansion: Int) -> UserCardGalleryViewModel(idUser = id, idExpansion = idExpansion, listUserCardUseCase = get(), quickSellUseCase = get(), userMainViewModel = get()) }
    viewModel { (id: Int, idExpansion: Int) -> UserCardCollectionViewModel(
        idUser = id, idExpansion = idExpansion,
        listUserCardCollectionUseCase = get()
    ) }
    viewModel { UserCardGalleryDetailViewModel(get(), get(), null) }
}