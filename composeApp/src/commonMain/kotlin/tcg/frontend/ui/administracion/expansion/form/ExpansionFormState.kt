package tcg.frontend.ui.administracion.expansion.form

data class ExpansionFormState (
    val name: String = "",
    val year: String = "",
    val price: String = "",

    val nameErr: String? = null,
    val yearErr: String? = null,
    val priceErr: String? = null,

    val isLoading: Boolean = false,
    val isExpansionSuccess: Boolean = false,
    val isValid: Boolean = false,

    val errorMessage: String? = null
)