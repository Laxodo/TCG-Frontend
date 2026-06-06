package tcg.frontend.ui.administracion

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.PermanentDrawerSheet
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.window.core.layout.WindowWidthSizeClass
import io.ktor.server.routing.Route
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import tcg.frontend.Routes
import tcg.frontend.ui.administracion.users.UserViewModel
import tcg.frontend.ui.administracion.users.Users
import tcg.frontend.ui.administracion.users.form.UserForm
import tcg.frontend.ui.administracion.users.form.UserFormViewModel
import tcg.frontend.ui.administracion.users.inventory.gallery.UserCardGalleryAdmin
import tcg.frontend.ui.administracion.users.inventory.gallery.detail.UserCardGalleryDetailAdmin
import tcg.frontend.ui.administracion.users.inventory.gallery.detail.UserCardGalleryDetailAdminViewModel
import tcg.frontend.ui.administracion.users.logs.LogView
import tcg.frontend.ui.administracion.users.logs.LogsViewModel
import tcg.frontend.ui.administracion.users.logs.detail.LogsViewDetail
import tcg.frontend.ui.usuario.expansion.Expansion
import tcg.frontend.ui.usuario.expansion.ExpansionViewModel
import tcg.frontend.ui.usuario.usercard.collectionView.UserCardCollection
import tcg.frontend.ui.usuario.usercard.collectionView.UserCardCollectionViewModel
import tcg.frontend.ui.usuario.usercard.galleryView.UserCardGalleryViewModel
import tcg.frontend.ui.usuario.usercard.galleryView.view.UserCardGalleryDetailViewModel
import tcg.frontend.ui.usuario.wiki.card.CardViewModel

@Composable
fun AdminMain(
    onLogout: () -> Unit
){
    val adminMainViewModel: AdminMainViewModel = koinViewModel()
    val expansionViewModel: ExpansionViewModel = koinViewModel()
    val userCardGalleryDetailViewModel: UserCardGalleryDetailViewModel = koinViewModel()
    val logsViewModel: LogsViewModel = koinViewModel()
    val userViewModel: UserViewModel = koinViewModel()
    val cardViewModel: CardViewModel = koinViewModel()
    val navController = rememberNavController()

    val options by adminMainViewModel.options.collectAsState()
    var screenState by remember { mutableStateOf(true) }
    val window = currentWindowAdaptiveInfo()


    adminMainViewModel.setOptions(
        listOf(
            ItemOption(
                Icons.Default.Person, {
                    navController.navigate(Routes.USERS){
                        launchSingleTop = true
                    }
                },
                "Usuarios"
            ),
            ItemOption(
                Icons.Default.Logout, {
                    onLogout()
                },
                "Cerrar sesión"
            )
        )
    )

    val navegador: @Composable () -> Unit = {
        NavHost(
            navController = navController,
            startDestination = Routes.ACCESS
        ){
            composable(Routes.ACCESS){
                Access()
            }

            composable(Routes.USERS){
                Users(
                    userViewModel,
                    {
                        userViewModel.setSelectedUser(it)
                        userViewModel.editMode = true
                        navController.navigate(Routes.USER){
                            launchSingleTop = true
                        }
                    },{
                        userViewModel.setSelectedUser(it)
                        userViewModel.editMode = false
                        navController.navigate(Routes.USER){
                            launchSingleTop = true
                        }
                    },
                    {
                        userViewModel.setSelectedUser(it)
                        navController.navigate(Routes.EXPANSIONS){
                            launchSingleTop = true
                        }
                    },
                    {
                        userViewModel.deleteUser(it)
                    },
                    {
                        logsViewModel.setUser(it)
                        navController.navigate(Routes.LOGS){
                            launchSingleTop = true
                        }
                    }
                )
            }

            composable(Routes.USER){
                val userFormViewModel: UserFormViewModel = koinViewModel<UserFormViewModel>{
                    parametersOf(userViewModel.selected.value, {})
                }
                UserForm(userViewModel, userFormViewModel,
                    {
                        navController.popBackStack()
                    },
                    {
                        userViewModel.updateUser(it)
                        navController.popBackStack()
                    }
                )
            }

            composable(Routes.EXPANSIONS){
                Expansion(expansionViewModel, {
                    expansionViewModel.setSelectedExpansion(it)
                    navController.navigate(Routes.USERCARDS){
                        launchSingleTop = true
                    }
                }, {
                    navController.popBackStack()
                })
            }

            composable(Routes.USERCARDS) {
                val userCardGalleryViewModel: UserCardGalleryViewModel = koinViewModel<UserCardGalleryViewModel>{
                    parametersOf(userViewModel.selected.value?.id, expansionViewModel.selected.value?.id)
                }
                val userCardCollectionViewModel: UserCardCollectionViewModel = koinViewModel<UserCardCollectionViewModel> {
                    parametersOf(userViewModel.selected.value?.id, expansionViewModel.selected.value?.id)
                }

                if (screenState) {
                    UserCardGalleryAdmin(
                        userCardGalleryViewModel,
                        {
                            userCardGalleryDetailViewModel.setSelectedUserCard(it)
                            cardViewModel.getExpansionCards(it.card.idExpansion)
                            navController.navigate(Routes.USERCARD){
                                launchSingleTop = true
                            }
                        },
                        {
                            screenState = !screenState
                        },
                        {
                            navController.popBackStack()
                        },
                        {
                            userCardGalleryViewModel.refresh()
                            userCardCollectionViewModel.refresh()
                        }
                    )
                } else {
                    UserCardCollection(
                        userCardCollectionViewModel,
                        {
                            navController.popBackStack()
                        },
                        {
                            screenState = !screenState
                        },
                        {
                            userCardGalleryViewModel.refresh()
                            userCardCollectionViewModel.refresh()
                        }
                    )
                }
            }

            composable(Routes.USERCARD) {
                UserCardGalleryDetailAdmin(
                    userCardGalleryDetailViewModel,
                    {
                        navController.popBackStack()
                    }
                )
            }

            composable(Routes.LOGS){
                LogView(
                    logsViewModel,
                    {
                        logsViewModel.setLog(it)
                        navController.navigate(Routes.LOG){
                            launchSingleTop = true
                        }
                    },
                    {
                        navController.popBackStack()
                    }
                )
            }

            composable(Routes.LOG){
                LogsViewDetail(
                    logsViewModel.log.value!!,
                    {
                        navController.popBackStack()
                    }
                )
            }

        }
    }

    if (window.windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.COMPACT) {
        Scaffold(
            bottomBar = {
                NavigationBar {
                    adminMainViewModel.options.collectAsState().value.forEach { item ->
                        NavigationBarItem(
                            selected = true,
                            onClick = { item.action() },
                            icon = { Icon(item.icon, contentDescription = item.name) },
                        )
                    }
                }
            }
        ) { innerPadding ->
            Box(Modifier.padding(innerPadding)) {
                navegador()
            }
        }
    } else {
        PermanentNavigationDrawer(
            drawerContent = {
                PermanentDrawerSheet(
                    Modifier.then(
                        if (window.windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.COMPACT)
                            Modifier.width(128.dp)
                        else Modifier.width(128.dp)
                    )
                ) {
                    Column(
                        modifier = Modifier.fillMaxHeight()
                            .padding(vertical = 16.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(Modifier.height(16.dp))
                        options.forEach { item ->
                            NavigationDrawerItem(
                                icon = {
                                    Box(
                                        modifier = Modifier.fillMaxWidth(),
                                        contentAlignment = Alignment.Center,

                                        ) {
                                        Icon(
                                            item.icon,
                                            tint = MaterialTheme.colorScheme.primary,
                                            contentDescription = item.name
                                        )
                                    }
                                },
                                label = { window.windowSizeClass.toString() },
                                selected = false,
                                onClick = { item.action() },
                                modifier = Modifier
                                    .padding(vertical = 4.dp)

                            )
                        }
                    }
                }
            },
            content = {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .height(600.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    navegador()
                }
            }
        )
    }
}