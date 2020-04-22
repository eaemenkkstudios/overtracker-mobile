package studios.eaemenkk.overtracker.interactor

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import studios.eaemenkk.overtracker.respository.AuthRepository
import java.lang.Exception

class AuthInteractor {
    private val firebaseRepository = AuthRepository()

    fun login(email: String, password: String, callback: (authResult: Task<AuthResult>) -> Unit) {
            if(email.isEmpty()) throw Exception("Por favor informe seu email!")

            if(password.isEmpty()) throw Exception("Por favor informe a senha!")
            else if(password.length < 6) throw Exception("O tamanho mínimo de senha é 6 caracteres.")

        firebaseRepository.login(email, password, callback)
    }

    fun register(email: String, password: String, confirmPassword: String, callback: (authResult: Task<AuthResult>) -> Unit) {
        if(email.isEmpty()) throw Exception("Por favor informe seu email!")

        if(password.isEmpty()) throw Exception("Por favor informe a senha!")
        else if(password.length < 6) throw Exception("O tamanho mínimo de senha é 6 caracteres.")

        if(password != confirmPassword) throw Exception("As senhas digitadas não conferem.")

        firebaseRepository.register(email, password, callback)
    }

    fun forgotPassword(email: String, callback: (authResult: Task<Void>) -> Unit) {
        if(email.isEmpty()) throw Exception("Por favor informe seu email!")

        firebaseRepository.forgotPassword(email, callback)
    }
}