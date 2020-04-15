package studios.eaemenkk.overtracker.view.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import studios.eaemenkk.overtracker.R

class LoginActivity : AppCompatActivity() {

    private val mAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btSignIn.setOnClickListener { signIn() }
        btSignUp.setOnClickListener { signUp() }
    }

    private fun signIn() {
        val email = etEmail.text.toString()
        val password = etPassword.text.toString()

        if (email.isEmpty()) {
            Toast.makeText(this, "E-mail obrigatório!", Toast.LENGTH_LONG).show()
            return
        }

        if (password.isEmpty()) {
            Toast.makeText(this, "Senha obrigatória!", Toast.LENGTH_LONG).show()
            return
        } else {
            if (password.length < 6) {
                Toast.makeText(
                    this,
                    "A senha precisa ter 6 caracteres no mínimo.",
                    Toast.LENGTH_LONG
                ).show()
                return
            }
        }

        val operation = mAuth.signInWithEmailAndPassword(email, password)
        operation.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val intentMain = Intent(this, MainActivity::class.java)
                startActivity(intentMain)
            } else {
                val error = task.exception?.localizedMessage
                    ?: "Não foi possível entrar no aplicativo no momento"
                Toast.makeText(this, error, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun signUp() {
        // val intentMain = Intent(this, SignUpActivity::class.java)
        // startActivity(intentMain)
    }

}