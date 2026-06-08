package tcg.frontend.ui.usuario.market

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import tcg.frontend.ui.usuario.ItemOption

data class ItemOption(
    val icon: ImageVector,
    val action:()->Unit,
    val name:String
)

class MarketViewModel(): ViewModel() {
    private val _options= MutableStateFlow<List<ItemOption>>(emptyList())
    val options = _options

    fun setOptions(options:List<ItemOption>){
        _options.value = options.toList()
    }
}