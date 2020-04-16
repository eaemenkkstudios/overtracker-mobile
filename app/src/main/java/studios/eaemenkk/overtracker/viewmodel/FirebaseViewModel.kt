package studios.eaemenkk.overtracker.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import studios.eaemenkk.overtracker.interactor.FirebaseInteractor

class FirebaseViewModel(app: Application) : AndroidViewModel(app) {
    private val interactor = FirebaseInteractor()

    val loginMsg = MutableLiveData<String>()
    val signUpMsg = MutableLiveData<String>()

    fun login(email: String, password: String) {
        interactor.login(email, password) { task ->
            if(task.isSuccessful) {
                loginMsg.value = "Login realizado com sucesso!"
                println("Done")
            } else {
                loginMsg.value = task.exception?.localizedMessage
            }
        }
    }

    fun register(email: String, password: String, confirmPassword: String) {
        interactor.register(email, password, confirmPassword) { task ->
            if(task.isSuccessful) {
                signUpMsg.value = "Cadastro realizado com sucesso!"
            } else {
                signUpMsg.value = task.exception?.localizedMessage
            }
        }
    }
}