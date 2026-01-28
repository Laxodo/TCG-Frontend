package tcg.frontend

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform