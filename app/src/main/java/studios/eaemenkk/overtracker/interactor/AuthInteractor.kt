package studios.eaemenkk.overtracker.interactor

import android.content.Context
import studios.eaemenkk.overtracker.R
import studios.eaemenkk.overtracker.domain.RequestResult
import studios.eaemenkk.overtracker.respository.AuthRepository

class AuthInteractor(val context: Context) {
   private val repository = AuthRepository(context, context.getString(R.string.api_base_url))

    fun login(session: String, callback: (authResult: RequestResult) -> Unit) {
        repository.login(session, callback)
    }

    fun logout(callback: (authResult: RequestResult) -> Unit) {
        repository.logout(callback)
    }

    fun isAuth(callback: (authResult: RequestResult) -> Unit) {
        repository.isAuth(callback)
    }
}