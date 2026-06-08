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
import tcg.frontend.infraestructura.entities.expansion.CreateExpansionRequest
import tcg.frontend.ui.administracion.card.Card
import tcg.frontend.ui.administracion.card.CardBatchCreate
import tcg.frontend.ui.administracion.card.CardCreate
import tcg.frontend.ui.administracion.card.CardViewModel
import tcg.frontend.ui.administracion.expansion.Expansion
import tcg.frontend.ui.administracion.expansion.ExpansionViewModel
import tcg.frontend.ui.administracion.expansion.ExpansionCreate
import tcg.frontend.ui.administracion.generation.Generation
import tcg.frontend.ui.administracion.generation.GenerationCreate
import tcg.frontend.ui.administracion.generation.GenerationViewModel
import tcg.frontend.ui.administracion.users.UserViewModel
import tcg.frontend.ui.administracion.users.Users
import tcg.frontend.ui.administracion.users.form.UserForm
import tcg.frontend.ui.administracion.users.form.UserFormViewModel

@Composable
fun AdminMain(
    onLogout: () -> Unit
){
    val adminMainViewModel: AdminMainViewModel = koinViewModel()
    val navController = rememberNavController()

    val options by adminMainViewModel.options.collectAsState()
    val window = currentWindowAdaptiveInfo()

    val userViewModel: UserViewModel = koinViewModel()
    val generationViewModel: GenerationViewModel = koinViewModel()
    val expansionViewModel: ExpansionViewModel = koinViewModel()
    val cardViewModel: CardViewModel = koinViewModel()

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
                Icons.Default.Style, {
                    navController.navigate(Routes.GENERATIONS) {
                        launchSingleTop = true
                    }
                },
                "Generacion"
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
                userViewModel.refresh()
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
                        userViewModel.deleteUser(it)
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
                    {}
                )
            }

            composable(Routes.GENERATIONS) {
                Generation(
                    generationViewModel,
                    onViewForm = {
                        generationViewModel.setSelectedGeneration(it)
                        expansionViewModel.setGeneration(it.id)
                        navController.navigate(Routes.EXPANSIONS)
                    },
                    onCreate = {
                        navController.navigate(Routes.GENERATION_CREATE)
                    },
                    onBack = {
                        navController.popBackStack()
                    }
                )
            }

            composable(Routes.GENERATION_CREATE) {
                GenerationCreate(
                    generationViewModel = generationViewModel,
                    onBack = {
                        navController.popBackStack()
                    }
                )
            }

            composable(Routes.EXPANSIONS) {
                Expansion(
                    expansionViewModel,
                    onViewForm = {
                        expansionViewModel.setSelectedExpansion(it)
                        cardViewModel.setExpansion(it.id)
                        navController.navigate(Routes.CARDS)
                    },
                    onCreate = {
                        navController.navigate(Routes.EXPANSION_CREATE)
                    },
                    onBack = {
                        navController.popBackStack()
                    }
                )
            }

            composable(Routes.EXPANSION_CREATE) {
                ExpansionCreate(
                    expansionViewModel = expansionViewModel,
                    generationId = expansionViewModel.generationId.value ?: return@composable,
                    onBack = {
                        navController.popBackStack()
                    }
                )
            }

            composable(Routes.CARDS) {
                Card(
                    cardViewModel = cardViewModel,
                    onCreate = {
                        navController.navigate(Routes.CARDS_CREATE)
                    },
                    onCreateBatch = {
                        navController.navigate(Routes.CARDS_BATCH_CREATE)
                    },
                    onBack = {
                        navController.popBackStack()
                    }
                )
            }

            composable(Routes.CARDS_CREATE) {
                val expansionId = expansionViewModel.selected.value?.id ?: return@composable
                CardCreate(
                    cardViewModel = cardViewModel,
                    expansionId = expansionId,
                    onPickImage = suspend { null },
                    onBack = { navController.popBackStack() }
                )
            }

            composable(Routes.CARDS_BATCH_CREATE) {
                val expansionId = expansionViewModel.selected.value?.id ?: return@composable

                CardBatchCreate(
                    cardViewModel = cardViewModel,
                    expansionId = expansionId,
                    onPickImage = suspend { null },
                    onBack = {
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