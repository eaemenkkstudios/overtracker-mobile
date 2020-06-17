package studios.eaemenkk.overtracker.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import studios.eaemenkk.overtracker.domain.RequestResult
import studios.eaemenkk.overtracker.interactor.AuthInteractor

class AuthViewModel(app: Application) : AndroidViewModel(app) {
    private val interactor = AuthInteractor(app.applicationContext)

    val loginMsg = MutableLiveData<RequestResult>()
    val isAuthMsg = MutableLiveData<RequestResult>()
    val logoutMsg = MutableLiveData<RequestResult>()

    fun login(session: String) {
        interactor.login(session) { result -> loginMsg.value = result }
    }

    fun logout() {
        interactor.logout { result -> logoutMsg.value = result  }
    }

    fun isAuth() {
        interactor.isAuth { result -> isAuthMsg.value = result }
    }
}