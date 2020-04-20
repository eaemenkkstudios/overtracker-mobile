package studios.eaemenkk.overtracker.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import studios.eaemenkk.overtracker.domain.AuthResult
import studios.eaemenkk.overtracker.interactor.AuthInteractor

class AuthViewModel(app: Application) : AndroidViewModel(app) {
    private val interactor = AuthInteractor()

    val loginMsg = MutableLiveData<AuthResult>()
    val signUpMsg = MutableLiveData<AuthResult>()
    val forgotPasswordMsg = MutableLiveData<AuthResult>()

    fun login(email: String, password: String) {
        loginMsg.value = AuthResult(false, "")
        interactor.login(email, password) { task ->
            if(task.isSuccessful) {
                loginMsg.value = AuthResult(true, "Login realizado com sucesso!")
            } else {
                loginMsg.value = AuthResult(false, task.exception?.localizedMessage.toString())
            }
        }
    }

    fun register(email: String, password: String, confirmPassword: String) {
        signUpMsg.value = AuthResult(false, "")
        interactor.register(email, password, confirmPassword) { task ->
            if(task.isSuccessful) {
                signUpMsg.value = AuthResult(true, "Cadastro realizado com sucesso!")
            } else {
                signUpMsg.value = AuthResult(false, task.exception?.localizedMessage.toString())
            }
        }
    }

    fun forgotPassword(email: String) {
        forgotPasswordMsg.value = AuthResult(false, "")
        interactor.forgotPassword(email) {task ->
            if(task.isSuccessful) {
                forgotPasswordMsg.value = AuthResult(true, "Email de recuperação de senha enviado com sucesso!")
            } else {
                forgotPasswordMsg.value = AuthResult(false, task.exception?.localizedMessage.toString())
            }
        }
    }
}