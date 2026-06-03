package tcg.frontend.ui.administracion.expansion.form

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ExpansionFormViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ExpansionFormState())

    val uiState = _uiState.asStateFlow()

    fun onNameChange(v: String) {
        _uiState.value = _uiState.value.copy(name = v)
    }

    fun onYearChange(v: String) {
        _uiState.value = _uiState.value.copy(year = v)
    }

    fun onPriceChange(v: String) {
        _uiState.value = _uiState.value.copy(price = v)
    }

    fun validate(): Boolean {
        val state = _uiState.value
        val nameError = if (state.name.isBlank()) "Campo obligatorio" else null

        val yearError = if (state.year.toIntOrNull() == null) "Año inválido" else null

        val priceError = if (state.price.toDoubleOrNull() == null) "Precio inválido" else null

        _uiState.value =
            state.copy(
                nameErr = nameError,
                yearErr = yearError,
                priceErr = priceError
            )

        return nameError == null &&
                yearError == null &&
                priceError == null
    }
}