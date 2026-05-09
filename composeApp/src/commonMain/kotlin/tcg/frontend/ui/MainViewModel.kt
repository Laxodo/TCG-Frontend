package tcg.frontend.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import tcg.frontend.aplicacion.UserSessionManager


class MainViewModel(private val sessionManager: UserSessionManager): ViewModel() {
    val currentUserState = sessionManager.currentUser

    init {
        viewModelScope.launch {
            sessionManager.session()
        }
    }

    fun logout(){
        sessionManager.logout()
    }
}