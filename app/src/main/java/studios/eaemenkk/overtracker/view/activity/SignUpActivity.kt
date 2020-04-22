package studios.eaemenkk.overtracker.view.activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_sign_up.*
import studios.eaemenkk.overtracker.R
import studios.eaemenkk.overtracker.viewmodel.AuthViewModel

class SignUpActivity : AppCompatActivity() {

    private lateinit var viewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        viewModel = ViewModelProvider(this).get(AuthViewModel::class.java)
        viewModel.signUpMsg.observe(this, Observer { result ->
            if(result.msg != "") {
                Toast.makeText(this.applicationContext, result.msg, Toast.LENGTH_SHORT).show()
                if(result.status) finish()
                else signupLoadingContainer.visibility = View.GONE
            }
        })

        btSignup.setOnClickListener { signUp() }
    }

    private fun signUp() {
        val email = etEmail.text.toString()
        val password = etPassword.text.toString()
        val confirmPassword = etConfirmPassword.text.toString()
        signupLoadingContainer.visibility = View.VISIBLE
        try {
            viewModel.register(email, password, confirmPassword)
        } catch (e: Exception) {
            signupLoadingContainer.visibility = View.GONE
            Toast.makeText(this.applicationContext, e.message, Toast.LENGTH_SHORT).show()
        }
    }
}