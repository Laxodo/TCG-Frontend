package tcg.frontend.ui.usuario.market

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Shop
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.PermanentDrawerSheet
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.window.core.layout.WindowWidthSizeClass
import org.koin.compose.viewmodel.koinViewModel
import tcg.frontend.Routes
import tcg.frontend.ui.usuario.ItemOption
import tcg.frontend.ui.usuario.UserMainViewModel
import tcg.frontend.ui.usuario.market.canceloffer.CancelOffers
import tcg.frontend.ui.usuario.market.canceloffer.CancelOffersViewModel
import tcg.frontend.ui.usuario.market.exchangeoffers.ExchangeOffers
import tcg.frontend.ui.usuario.market.exchangeoffers.ExchangeOffersViewModel
import tcg.frontend.ui.usuario.market.exchangeoffers.view.ExchangeOffersView
import tcg.frontend.ui.usuario.market.selloffers.SellOffers
import tcg.frontend.ui.usuario.market.selloffers.SellOffersViewModel
import tcg.frontend.ui.usuario.market.selloffers.view.SellOffersView

@Composable
fun Market() {
    val marketViewModel: MarketViewModel = koinViewModel()
    val userMainViewModel: UserMainViewModel = koinViewModel()
    val sellOffersViewModel: SellOffersViewModel = koinViewModel()
    val exchangeOffersViewModel: ExchangeOffersViewModel = koinViewModel()
    val cancelOffersViewModel: CancelOffersViewModel = koinViewModel()
    val navController = rememberNavController()

    val options by marketViewModel.options.collectAsState()
    val window = currentWindowAdaptiveInfo()

    marketViewModel.setOptions(
        listOf(
            ItemOption(
                Icons.Default.Shop, {
                    navController.navigate(Routes.EXCHANGEOFFERS){
                        launchSingleTop = true
                    }
                },
                "Ver ofertas de venta"
            ),
            ItemOption(
                Icons.Default.Shop, {
                    navController.navigate(Routes.SELLOFFERS){
                        launchSingleTop = true
                    }
                },
                "Ver ofertas de venta"
            ),
            ItemOption(
                Icons.Default.Inventory2, {
                    navController.navigate(Routes.VIEWSELFOFFERS){
                        launchSingleTop = true
                    }
                },
                "Ver mis ofertas"
            )
        )
    )

    val navegador: @Composable () -> Unit = {
        NavHost(
            navController = navController,
            startDestination = Routes.SELLOFFERS
        ) {
            composable(Routes.SELLOFFERS) {
                SellOffers(
                    sellOffersViewModel,
                    {
                        sellOffersViewModel.setSelectedOffer(it)
                        navController.navigate(Routes.SELLOFFERSVIEW){
                            launchSingleTop = true
                        }
                    },
                    {
                        navController.popBackStack()
                    }
                )
            }

            composable(Routes.SELLOFFERSVIEW){
                SellOffersView(
                    sellOffersViewModel,
                    userMainViewModel,
                    {
                        sellOffersViewModel.buyOffer(it)
                        navController.popBackStack()
                    },
                    {
                        navController.popBackStack()
                    }
                )
            }

            composable(Routes.EXCHANGEOFFERS) {
                ExchangeOffers(
                    exchangeOffersViewModel,
                    {
                        navController.navigate(Routes.EXCHANGEOFFERSVIEW){
                            launchSingleTop = true
                        }
                    },
                    {

                    }
                )
            }

            composable(Routes.EXCHANGEOFFERSVIEW) {
                ExchangeOffersView(
                    exchangeOffersViewModel,
                    {
                        exchangeOffersViewModel.buyOffer(it)
                    },
                    {
                        navController.popBackStack()
                    }
                )
            }

            composable(Routes.VIEWSELFOFFERS) {
                CancelOffers(
                    cancelOffersViewModel,
                    {
                        cancelOffersViewModel.cancelOffer(it)
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
            topBar = {
                NavigationBar {
                    marketViewModel.options.collectAsState().value.forEach { item ->
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