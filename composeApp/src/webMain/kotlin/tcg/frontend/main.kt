package tcg.frontend

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import org.koin.core.context.startKoin
import tcg.frontend.di.appModel

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    startKoin {
        modules(appModel)
    }
    ComposeViewport {
        App()
    }
}