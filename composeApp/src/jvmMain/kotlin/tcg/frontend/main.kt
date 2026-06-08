package tcg.frontend

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.koin.core.context.GlobalContext.startKoin
import tcg.frontend.di.appModel

fun main() = application {
    startKoin {
        modules(appModel)
    }
    Window(
        onCloseRequest = ::exitApplication,
        title = "TCG-Frontend",
    ) {
        App()
    }
}