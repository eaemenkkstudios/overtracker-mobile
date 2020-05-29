package studios.eaemenkk.overtracker.interactor

import android.content.Context
import com.google.android.gms.tasks.Task
import studios.eaemenkk.overtracker.R
import studios.eaemenkk.overtracker.domain.RequestResult
import studios.eaemenkk.overtracker.respository.AuthRepository
import java.lang.Exception

class AuthInteractor(val context: Context) {
   private val firebaseRepository = AuthRepository(context, context.getString(R.string.api_base_url))

    fun login(session: String, callback: (authResult: RequestResult) -> Unit) {
        firebaseRepository.login(session, callback)
    }

    fun logout(callback: (authResult: RequestResult) -> Unit) {
        firebaseRepository.logout(callback)
    }
}