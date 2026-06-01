package tcg.frontend.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.koin.compose.viewmodel.koinViewModel
import tcg.frontend.Routes
import tcg.frontend.ui.register.Register
import tcg.frontend.ui.administracion.AdminMain
import tcg.frontend.ui.login.Login
import tcg.frontend.ui.usuario.UserMain

@Composable
fun Main() {
    val mainViewModel: MainViewModel = koinViewModel()
    val navController = rememberNavController()
    val user = mainViewModel.currentUserState.collectAsState()

    NavHost(
        navController = navController,
        startDestination = if (user.value != null) Routes.ACCESS else Routes.LOGIN
    ){
        composable(Routes.LOGIN) {
            Login({
                    navController.navigate(Routes.REGISTER) {
                        launchSingleTop = true
                    }
            })
        }

        composable(Routes.REGISTER) {
            Register({
                navController.popBackStack()
            }, {
                navController.navigate(Routes.LOGIN) {
                    launchSingleTop = true
                }
            })
        }

        composable(Routes.ACCESS){
            if(user.value?.isAdmin ?: false){
                AdminMain({
                        mainViewModel.logout()
                })
            }else{
                UserMain(mainViewModel,{
                    mainViewModel.logout()
                })
            }
        }
    }
}