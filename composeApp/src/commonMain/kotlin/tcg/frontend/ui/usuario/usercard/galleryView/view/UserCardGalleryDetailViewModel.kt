package tcg.frontend.ui.usuario.usercard.galleryView.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import tcg.frontend.aplicacion.market.grade.GradeCardCommand
import tcg.frontend.aplicacion.market.grade.GradeCardUseCase
import tcg.frontend.aplicacion.market.offers.exchange.ExchangeCardCommand
import tcg.frontend.aplicacion.market.offers.exchange.ExchangeCardUseCase
import tcg.frontend.aplicacion.market.sell.SellOfferCommand
import tcg.frontend.aplicacion.market.sell.SellOfferUseCase
import tcg.frontend.aplicacion.market.quicksell.QuickSellCommand
import tcg.frontend.aplicacion.market.quicksell.QuickSellUseCase
import tcg.frontend.aplicacion.usercard.listar.UserCardDTO
import tcg.frontend.dominio.Card
import tcg.frontend.ui.usuario.UserMainViewModel

data class UserCardGalleryState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
data class UserCardDetailState(
    val price: String = "",
    val psa: String? = null,
    val psaErr: String? = null,
    val priceErr: String? = null
)
class UserCardGalleryDetailViewModel(
    private val userMainViewModel: UserMainViewModel,
    private val gradeCardUseCase: GradeCardUseCase,
    private val exchangeCardUseCase: ExchangeCardUseCase,
    private val quickSellUseCase: QuickSellUseCase,
    private val sellOfferUseCase: SellOfferUseCase,
    item: UserCardDTO?
): ViewModel() {
    private val _usercard = MutableStateFlow(item)
    val userCard = _usercard.asStateFlow()

    private val _state = MutableStateFlow(UserCardGalleryState())
    val state: StateFlow<UserCardGalleryState> = _state.asStateFlow()

    private val  _exchangeCard = MutableStateFlow<Card?>(null)
    val exchangeCard: StateFlow<Card?> = _exchangeCard.asStateFlow()

    private val _uiState = MutableStateFlow(UserCardDetailState(
        price = item?.userCard?.price?.toString() ?: "",
        priceErr = null
    ))
    val uiState: StateFlow<UserCardDetailState> = _uiState.asStateFlow()

    fun setSelectedUserCard(item: UserCardDTO){
        _usercard.value = item
    }

    fun setExchangeCard(item: Card){
        _exchangeCard.value = item
    }

    fun onChangePrice(v: String){
        _uiState.value = _uiState.value.copy(price = v, priceErr = validatePrice(v))
    }

    fun onPSAChange(v: String){
        _uiState.value = _uiState.value.copy(psa = v, psaErr = validatePSA(v))
    }

    fun validatePSA(v: String): String?{
        if (v.toIntOrNull() == null) return "El formato no es valido"
        if (v.toInt() !in 1..10) return "El psa debe de ser superior a 0 e inferior a 11"
        return null
    }

    fun validatePrice(v: String): String?{
        if (v.toFloatOrNull() == null) return "El formato no es valido"
        if (v.toFloat() < 0) return "El precio debe de ser superior a 0"
        return null
    }

    fun sellCard(){
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }
                sellOfferUseCase.invoke(SellOfferCommand(_usercard.value?.userCard?.id ?: -999, _uiState.value.price.toFloat()))
                .onSuccess {
                    _state.update { it.copy(isLoading = false) }
                    userMainViewModel.refreshUser()
                }
                .onFailure { error ->
                    _state.update { it.copy(errorMessage = error.message, isLoading = false) }
                }
        }
    }

    fun exchangeCard(){
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }
            exchangeCardUseCase.invoke(ExchangeCardCommand(
                id = _usercard.value?.userCard?.id ?: -999,
                idCard = _exchangeCard.value?.id ?: -999,
                psa = _uiState.value.psa?.toInt()
            ))
            .onSuccess {
                _state.update { it.copy(isLoading = false) }
            }
            .onFailure { error ->
                _state.update { it.copy(errorMessage = error.message, isLoading = false) }
            }
        }
    }

    fun gradeCard(){
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }
            gradeCardUseCase.invoke(GradeCardCommand(_usercard.value?.userCard?.id ?: -999))
                .onSuccess { grade ->
                    _usercard.value?.userCard?.psa = grade
                    userMainViewModel.refreshUser()
                    _state.update { it.copy(isLoading = false) }
                }
                .onFailure { error ->
                    _state.update { it.copy(errorMessage = error.message, isLoading = false) }
                }
        }
    }

    fun quickSell() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }
            quickSellUseCase.invoke(QuickSellCommand(listOf(_usercard.value?.userCard?.id ?: -999)))
                .onSuccess {
                    _state.update { it.copy(isLoading = false) }
                    userMainViewModel.refreshUser()
                }
                .onFailure { error ->
                    _state.update { it.copy(errorMessage = error.message, isLoading = false) }
                }
        }
    }
}