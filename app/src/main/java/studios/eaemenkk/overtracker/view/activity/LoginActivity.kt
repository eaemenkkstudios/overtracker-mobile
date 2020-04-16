package studios.eaemenkk.overtracker.view.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import studios.eaemenkk.overtracker.R
import studios.eaemenkk.overtracker.viewmodel.FirebaseViewModel
import java.lang.Exception

class LoginActivity : AppCompatActivity() {

    private var viewModel: FirebaseViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        viewModel = ViewModelProvider(this).get(FirebaseViewModel::class.java)

        btSignIn.setOnClickListener { signIn() }
        btSignUp.setOnClickListener { signUp() }
    }



    private fun signIn() {
        val email = etEmail.text.toString()
        val password = etPassword.text.toString()
        try {
            viewModel!!.login(email, password)
        } catch (e: Exception) {
            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
        }
        viewModel!!.loginMsg.observe(this, Observer { msg ->
            Toast.makeText(this.applicationContext, msg, Toast.LENGTH_LONG).show()
            if(msg == "Login realizado com sucesso!") {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        })
    }

    private fun signUp() {
        startActivity(Intent(this, SignUpActivity::class.java))
    }

}