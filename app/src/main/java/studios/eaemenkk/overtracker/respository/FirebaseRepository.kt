package studios.eaemenkk.overtracker.respository

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

class FirebaseRepository () {

    private val mAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

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

}
