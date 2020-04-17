package studios.eaemenkk.overtracker.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import studios.eaemenkk.overtracker.domain.FirebaseResult
import studios.eaemenkk.overtracker.interactor.FirebaseInteractor

class FirebaseViewModel(app: Application) : AndroidViewModel(app) {
    private val interactor = FirebaseInteractor()

    val loginMsg = MutableLiveData<FirebaseResult>()
    val signUpMsg = MutableLiveData<FirebaseResult>()

    fun login(email: String, password: String) {
        interactor.login(email, password) { task ->
            if(task.isSuccessful) {
                loginMsg.value = FirebaseResult(true, "Login realizado com sucesso!")
            } else {
                loginMsg.value = FirebaseResult(false, task.exception?.localizedMessage.toString())
            }
        }
    }

    fun register(email: String, password: String, confirmPassword: String) {
        interactor.register(email, password, confirmPassword) { task ->
            if(task.isSuccessful) {
                signUpMsg.value = FirebaseResult(true, "Cadastro realizado com sucesso!")
            } else {
                signUpMsg.value = FirebaseResult(false, task.exception?.localizedMessage.toString())
            }
        }
    }
}