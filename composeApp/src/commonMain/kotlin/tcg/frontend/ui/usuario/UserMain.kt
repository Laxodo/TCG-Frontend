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
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Euro
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Shop
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.PermanentDrawerSheet
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.Text
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
import tcg.frontend.ui.usuario.market.Market
import tcg.frontend.ui.usuario.openbooster.OpenBooster
import tcg.frontend.ui.usuario.openbooster.OpenBoosterViewModel
import tcg.frontend.ui.usuario.openbooster.view.OpenBoosterView
import tcg.frontend.ui.usuario.usercard.collectionView.UserCardCollection
import tcg.frontend.ui.usuario.usercard.collectionView.UserCardCollectionViewModel
import tcg.frontend.ui.usuario.usercard.galleryView.UserCardGallery
import tcg.frontend.ui.usuario.usercard.galleryView.UserCardGalleryViewModel
import tcg.frontend.ui.usuario.usercard.galleryView.view.UserCardGalleryDetail
import tcg.frontend.ui.usuario.usercard.galleryView.view.UserCardGalleryDetailViewModel
import tcg.frontend.ui.usuario.wiki.card.Card
import tcg.frontend.ui.usuario.wiki.card.CardViewModel
import tcg.frontend.ui.usuario.wiki.card.detail.CardDetail
import tcg.frontend.ui.usuario.wiki.generation.Generation
import tcg.frontend.ui.usuario.wiki.generation.GenerationViewModel

@Composable
fun UserMain(
    mainViewModel: MainViewModel,
    onLogout: () -> Unit
){
    val userMainViewModel: UserMainViewModel = koinViewModel()
    val expansionViewModel: ExpansionViewModel = koinViewModel()
    val userCardGalleryDetailViewModel: UserCardGalleryDetailViewModel = koinViewModel()
    val openBoosterViewModel: OpenBoosterViewModel = koinViewModel()
    val cardViewModel: CardViewModel = koinViewModel()
    val navController = rememberNavController()

    val options by userMainViewModel.options.collectAsState()
    val user by userMainViewModel.user.collectAsState()
    val window = currentWindowAdaptiveInfo()
    var screenState by remember { mutableStateOf(true) }
    var refresh = false // Sorry for this

    userMainViewModel.setOptions(
        listOf(
            ItemOption(
                Icons.Default.Euro, {
                    navController.navigate(Routes.GENERATION){
                        launchSingleTop = true
                    }
                },
                "Mercado"
            ),
            ItemOption(
                Icons.Default.Shop, {
                    navController.navigate(Routes.MARKET){
                        launchSingleTop = true
                    }
                },
                "Mercado"
            ),
            ItemOption(
                Icons.Default.AttachMoney, {
                    navController.navigate(Routes.OPENBOOSTER){
                        launchSingleTop = true
                    }
                },
                "Abrir sobres"
            ),
            ItemOption(
                Icons.Default.Inventory2, {
                    navController.navigate(Routes.EXPANSIONS){
                        launchSingleTop = true
                    }
                },
                "Inventario"
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

            composable(Routes.MARKET){
                Market()
            }

            composable(Routes.OPENBOOSTER) {
                OpenBooster(
                    userMainViewModel,
                    expansionViewModel,
                    {
                        openBoosterViewModel.openBoosted(it.id)
                        navController.navigate(Routes.OPENBOOSTERVIEW){
                            launchSingleTop = true
                        }
                    },
                    {
                        navController.popBackStack()
                    }
                )
            }

            composable(Routes.OPENBOOSTERVIEW) {
                OpenBoosterView(
                    userMainViewModel,
                    expansionViewModel,
                    openBoosterViewModel,
                    {
                        openBoosterViewModel.openBoosted(openBoosterViewModel.items.value.first().idExpansion)
                    },
                    {
                        navController.popBackStack()
                    }
                )
            }

            composable(Routes.GENERATION){
                val generationViewModel: GenerationViewModel = koinViewModel()
                Generation(
                    generationViewModel,
                    {
                        generationViewModel.setSelectedGeneration(it)
                        expansionViewModel.getGenerationExpansions(it.id)
                        navController.navigate(Routes.EXPANSIONSWIKI)
                    },
                    {
                        navController.popBackStack()
                    }
                )
            }

            composable(Routes.EXPANSIONSWIKI){
                Expansion(expansionViewModel,{
                    expansionViewModel.setSelectedExpansion(it)
                    cardViewModel.getExpansionCards(it.id)
                    navController.navigate(Routes.CARDS){
                        launchSingleTop = true
                    }
                },
                    {
                        navController.popBackStack()
                    })
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

            composable(Routes.CARDS){
                Card(
                    cardViewModel,
                    {
                        cardViewModel.setSelectedCard(it)
                        navController.navigate(Routes.CARD){
                            launchSingleTop = true
                        }
                    },
                    {
                        navController.popBackStack()
                    }
                )
            }

            composable(Routes.CARD){
                CardDetail(
                    cardViewModel,
                    {
                        navController.popBackStack()
                    }
                )
            }

            composable(Routes.USERCARDS) {
                val userCardGalleryViewModel: UserCardGalleryViewModel = koinViewModel<UserCardGalleryViewModel>{
                    parametersOf(mainViewModel.currentUserState.value?.id, expansionViewModel.selected.value?.id)
                }
                val userCardCollectionViewModel: UserCardCollectionViewModel = koinViewModel<UserCardCollectionViewModel> {
                    parametersOf(mainViewModel.currentUserState.value?.id, expansionViewModel.selected.value?.id)
                }

                if (refresh){ // Sorry for this
                    userCardGalleryViewModel.refresh()
                    userCardCollectionViewModel.refresh()
                    refresh = false
                }

                if (screenState) {
                    UserCardGallery(
                        userCardGalleryViewModel,
                        {
                            if (userCardGalleryViewModel.sellMode.value){
                                userCardGalleryViewModel.setSelectedUserCard(it)
                            }else{
                                userCardGalleryDetailViewModel.setSelectedUserCard(it)
                                cardViewModel.getExpansionCards(it.card.idExpansion)
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
                            userCardCollectionViewModel.refresh()
                        },
                        {
                            userCardGalleryViewModel.refresh()
                            userCardCollectionViewModel.refresh()
                            userMainViewModel.refreshUser()
                        },
                        {
                            userCardGalleryViewModel.setSelectedUsersCards(it)
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
                            userMainViewModel.refreshUser()
                        }
                    )
                }
            }

            // TODO: This prints multiples times, i dont know why.
            composable(Routes.USERCARD){
                UserCardGalleryDetail(
                    userCardGalleryDetailViewModel,
                    cardViewModel,
                    {
                        userCardGalleryDetailViewModel.quickSell()
                        refresh = true
                        navController.popBackStack()
                    },
                    {
                        userCardGalleryDetailViewModel.sellCard()
                        refresh = true
                        navController.popBackStack()
                    },
                    {
                        userCardGalleryDetailViewModel.gradeCard()
                        refresh = true
                    },
                    {
                        userCardGalleryDetailViewModel.exchangeCard()
                        refresh = true
                        navController.popBackStack()
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
                        Text(
                            text = if (user != null) "${user!!.money}€" else "0€",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(16.dp)
                        )
                        Spacer(modifier = Modifier.weight(1f))
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
                        Spacer(modifier = Modifier.weight(1f))
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