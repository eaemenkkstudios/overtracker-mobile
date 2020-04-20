package studios.eaemenkk.overtracker.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import studios.eaemenkk.overtracker.domain.FirebaseResult
import studios.eaemenkk.overtracker.interactor.AuthInteractor

class AuthViewModel(app: Application) : AndroidViewModel(app) {
    private val interactor = AuthInteractor()

    val loginMsg = MutableLiveData<FirebaseResult>()
    val signUpMsg = MutableLiveData<FirebaseResult>()
    val forgotPasswordMsg = MutableLiveData<FirebaseResult>()

    fun login(email: String, password: String) {
        loginMsg.value = FirebaseResult(false, "")
        interactor.login(email, password) { task ->
            if(task.isSuccessful) {
                loginMsg.value = FirebaseResult(true, "Login realizado com sucesso!")
            } else {
                loginMsg.value = FirebaseResult(false, task.exception?.localizedMessage.toString())
            }
        }
    }

    fun register(email: String, password: String, confirmPassword: String) {
        signUpMsg.value = FirebaseResult(false, "")
        interactor.register(email, password, confirmPassword) { task ->
            if(task.isSuccessful) {
                signUpMsg.value = FirebaseResult(true, "Cadastro realizado com sucesso!")
            } else {
                signUpMsg.value = FirebaseResult(false, task.exception?.localizedMessage.toString())
            }
        }
    }

    fun forgotPassword(email: String) {
        forgotPasswordMsg.value = FirebaseResult(false, "")
        interactor.forgotPassword(email) {task ->
            if(task.isSuccessful) {
                forgotPasswordMsg.value = FirebaseResult(true, "Email de recuperação de senha enviado com sucesso!")
            } else {
                forgotPasswordMsg.value = FirebaseResult(false, task.exception?.localizedMessage.toString())
            }
        }
    }
}