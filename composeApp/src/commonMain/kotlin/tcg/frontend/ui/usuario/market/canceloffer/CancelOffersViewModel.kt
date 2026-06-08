package tcg.frontend.ui.usuario.market.canceloffer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import tcg.frontend.aplicacion.market.offers.buy.BuyOfferCommand
import tcg.frontend.aplicacion.market.offers.buy.BuyOfferUseCase
import tcg.frontend.aplicacion.market.offers.cancel.CancelOfferCommand
import tcg.frontend.aplicacion.market.offers.cancel.CancelOfferUseCase
import tcg.frontend.aplicacion.market.offers.list.ListOffersCommand
import tcg.frontend.aplicacion.market.offers.list.ListOffersUseCase
import tcg.frontend.dominio.Offer
import tcg.frontend.ui.usuario.UserMainViewModel

data class SellOfferState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
class CancelOffersViewModel(
    private val userMainViewModel: UserMainViewModel,
    private val listOffersUseCase: ListOffersUseCase,
    private val cancelOfferUseCase: CancelOfferUseCase
): ViewModel() {
    private val _items = MutableStateFlow<MutableList<Offer>>(mutableListOf())
    val items: StateFlow<List<Offer>> = _items.asStateFlow()

    private val _state = MutableStateFlow(SellOfferState())
    val state: StateFlow<SellOfferState> = _state.asStateFlow()

    init {
        refresh()
    }

    fun cancelOffer(item: Offer){
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }
            cancelOfferUseCase.invoke(CancelOfferCommand(item.id))
                .onSuccess {
                    _state.update { it.copy(isLoading = false) }
                    refresh()
                }
                .onFailure { error ->
                    _state.update { it.copy(errorMessage = error.message, isLoading = false) }
                    refresh()
                }
        }
    }

    fun refresh(){
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }
            listOffersUseCase.invoke(ListOffersCommand(id = userMainViewModel.user.value?.id ?: -999))
                .onSuccess { usercards ->
                    _state.update { it.copy(isLoading = false) }
                    _items.value.clear()
                    _items.value = usercards as MutableList<Offer>
                }
                .onFailure { error ->
                    _state.update { it.copy(errorMessage = error.message, isLoading = false) }
                }
        }
    }
}