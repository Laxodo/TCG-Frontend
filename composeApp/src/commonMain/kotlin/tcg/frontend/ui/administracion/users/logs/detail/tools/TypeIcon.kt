package tcg.frontend.ui.administracion.users.logs.detail.tools

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.Verified
import androidx.compose.ui.graphics.vector.ImageVector

fun typeIcon(s: String): ImageVector {
    return when(s){
        Type.SALE.type -> Icons.Default.Storefront
        Type.EXCHANGE.type -> Icons.Default.SwapHoriz
        Type.OPENBOOSTER.type -> Icons.Default.CardGiftcard
        Type.QUICKSELL.type -> Icons.Default.MonetizationOn
        Type.GRADE.type -> Icons.Default.Verified
        else -> Icons.Default.ListAlt
    }
}