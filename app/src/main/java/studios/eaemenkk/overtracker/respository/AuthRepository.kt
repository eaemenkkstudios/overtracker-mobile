package studios.eaemenkk.overtracker.respository

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

class AuthRepository {

    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    fun login(email: String, password: String, callback: (authResult: Task<AuthResult>) -> Unit) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            callback(task)
        }
    }

    fun register(email: String, password: String, callback: (authResult: Task<AuthResult>) -> Unit) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            callback(task)
        }
    }

    fun forgotPassword(email: String, callback: (authResult: Task<Void>) -> Unit) {
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener{ task ->
            callback(task)
        }
    }

}
