package studios.eaemenkk.overtracker.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import studios.eaemenkk.overtracker.domain.RequestResult
import studios.eaemenkk.overtracker.interactor.AuthInteractor

class AuthViewModel(app: Application) : AndroidViewModel(app) {
    private val interactor = AuthInteractor()

    val loginMsg = MutableLiveData<RequestResult>()
    val signUpMsg = MutableLiveData<RequestResult>()
    val forgotPasswordMsg = MutableLiveData<RequestResult>()

    fun login(email: String, password: String) {
        interactor.login(email, password) { task ->
            if(task.isSuccessful) {
                loginMsg.value = RequestResult(true, "Login successful!")
            } else {
                loginMsg.value = RequestResult(false, task.exception?.localizedMessage.toString())
            }
        }
    }

    fun register(email: String, password: String, confirmPassword: String) {
        interactor.register(email, password, confirmPassword) { task ->
            if(task.isSuccessful) {
                signUpMsg.value = RequestResult(true, "Account created!")
            } else {
                signUpMsg.value = RequestResult(false, task.exception?.localizedMessage.toString())
            }
        }
    }

    fun forgotPassword(email: String) {
        interactor.forgotPassword(email) {task ->
            if(task.isSuccessful) {
                forgotPasswordMsg.value = RequestResult(true, "Password Recovery email sent!")
            } else {
                forgotPasswordMsg.value = RequestResult(false, task.exception?.localizedMessage.toString())
            }
        }
    }
}