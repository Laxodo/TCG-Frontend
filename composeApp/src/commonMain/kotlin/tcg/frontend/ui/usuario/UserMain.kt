package tcg.frontend.ui.usuario

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
import androidx.compose.material3.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Style
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
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.window.core.layout.WindowWidthSizeClass
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import tcg.frontend.Routes
import tcg.frontend.ui.MainViewModel
import tcg.frontend.ui.usuario.expansion.Expansion
import tcg.frontend.ui.usuario.expansion.ExpansionViewModel
import tcg.frontend.ui.usuario.usercard.collectionView.UserCardCollection
import tcg.frontend.ui.usuario.usercard.collectionView.UserCardCollectionViewModel
import tcg.frontend.ui.usuario.usercard.galleryView.UserCardGallery
import tcg.frontend.ui.usuario.usercard.galleryView.UserCardGalleryViewModel
import tcg.frontend.ui.usuario.usercard.galleryView.view.UserCardGalleryDetail
import tcg.frontend.ui.usuario.usercard.galleryView.view.UserCardGalleryDetailViewModel

@Composable
fun UserMain(
    mainViewModel: MainViewModel,
    onLogout: () -> Unit
){
    val userMainViewModel: UserMainViewModel = koinViewModel()
    val expansionViewModel: ExpansionViewModel = koinViewModel()
    val userCardGalleryDetailViewModel: UserCardGalleryDetailViewModel = koinViewModel()
    val navController = rememberNavController()

    val options by userMainViewModel.options.collectAsState()
    val window = currentWindowAdaptiveInfo()
    var screenState by remember { mutableStateOf(true) }

    userMainViewModel.setOptions(
        listOf(
            ItemOption(
                Icons.Default.Style, {
                    navController.navigate(Routes.EXPANSIONS){
                        launchSingleTop = true
                    }
                },
                "Expansiones"
            ),
            ItemOption(
                Icons.AutoMirrored.Filled.Logout, {
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

            composable(Routes.EXPANSIONS){
                Expansion(expansionViewModel,{
                    expansionViewModel.setSelectedExpansion(it)
                    navController.navigate(Routes.USERCARDS){
                        launchSingleTop = true
                    }
                },
                    {
                        navController.popBackStack()
                    })
            }

            // TODO: The gallery doesn't refresh when returning from the single card detail view.
            composable(Routes.USERCARDS) {
                val userCardGalleryViewModel: UserCardGalleryViewModel = koinViewModel<UserCardGalleryViewModel>{
                    parametersOf(mainViewModel.currentUserState.value?.id, expansionViewModel.selected.value?.id)
                }
                val userCardCollectionViewModel: UserCardCollectionViewModel = koinViewModel<UserCardCollectionViewModel> {
                    parametersOf(mainViewModel.currentUserState.value?.id, expansionViewModel.selected.value?.id)
                }

                if (screenState) {
                    UserCardGallery(
                        userCardGalleryViewModel,
                        {
                            if (userCardGalleryViewModel.sellMode.value){
                                userCardGalleryViewModel.setSelectedUserCard(it)
                            }else{
                                userCardGalleryDetailViewModel.setSelectedUserCard(it)
                                navController.navigate(Routes.USERCARD) {
                                    launchSingleTop = true
                                }
                            }
                        },
                        {
                            navController.popBackStack()
                        },
                        {
                            screenState = !screenState
                        },
                        {
                            userCardGalleryViewModel.changeSellMode()
                        },
                        {
                            userCardGalleryViewModel.quickSell()
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
                        }
                    )
                }
            }

            composable(Routes.USERCARD){
                UserCardGalleryDetail(
                    userCardGalleryDetailViewModel,
                    {
                        userCardGalleryDetailViewModel.quickSell()
                        navController.popBackStack()
                    },
                    {
                        TODO("The function has not been implemented yet.")
                    },
                    {
                        TODO("The function has not been implemented yet.")
                    },
                    {
                        TODO("The function has not been implemented yet.")
                    },
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
                    userMainViewModel.options.collectAsState().value.forEach { item ->
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