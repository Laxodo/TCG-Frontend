package tcg.frontend.infraestructura

import com.russhwolf.settings.Settings

class TokenStorage(private val settings: Settings) {
    companion object{
        const val ACCESS_TOKEN: String = "access_token"
    }
    fun saveTokens(accessToken: String){
        settings.putString(ACCESS_TOKEN, accessToken)
    }

    fun getAccessToken(): String? {
        return settings.getStringOrNull(ACCESS_TOKEN)
    }

    fun clear() {
        settings.remove(ACCESS_TOKEN)
    }
}