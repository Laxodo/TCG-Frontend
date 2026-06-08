package tcg.frontend.ui.usuario.market.exchangeoffers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import tcg.frontend.aplicacion.market.offers.buy.BuyOfferCommand
import tcg.frontend.aplicacion.market.offers.buy.BuyOfferUseCase
import tcg.frontend.aplicacion.market.offers.list.ListOffersCommand
import tcg.frontend.aplicacion.market.offers.list.ListOffersUseCase
import tcg.frontend.dominio.Offer
import tcg.frontend.ui.usuario.UserMainViewModel
import tcg.frontend.ui.usuario.market.tools.ExchangeType

data class ExchangeOfferState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
class ExchangeOffersViewModel(
    private val userMainViewModel: UserMainViewModel,
    private val listOffersUseCase: ListOffersUseCase,
    private val buyOfferUseCase: BuyOfferUseCase
): ViewModel() {
    private val _items = MutableStateFlow<MutableList<Offer>>(mutableListOf())
    val items: StateFlow<List<Offer>> = _items.asStateFlow()

    private val _state = MutableStateFlow(ExchangeOfferState())
    val state: StateFlow<ExchangeOfferState> = _state.asStateFlow()

    private val _selected = MutableStateFlow<Offer?>(null)
    val selected: StateFlow<Offer?> = _selected.asStateFlow()

    init {
        refresh()
    }

    fun setSelectedOffer(item: Offer){
        _selected.value = item
    }

    fun buyOffer(item: Offer){
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }
            buyOfferUseCase.invoke(BuyOfferCommand(item.id))
                .onSuccess {
                    _state.update { it.copy(isLoading = false) }
                    userMainViewModel.refreshUser()
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
            listOffersUseCase.invoke(ListOffersCommand(type = ExchangeType.EXCHANGES.type))
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