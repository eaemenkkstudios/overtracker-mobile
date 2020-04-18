package studios.eaemenkk.overtracker.view.activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_sign_up.*
import studios.eaemenkk.overtracker.R
import studios.eaemenkk.overtracker.viewmodel.FirebaseViewModel

class SignUpActivity : AppCompatActivity() {

    private var viewModel: FirebaseViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        viewModel = ViewModelProvider(this).get(FirebaseViewModel::class.java)
        btSignup.setOnClickListener { signUp() }
    }

    private fun signUp() {
        val email = etEmail.text.toString()
        val password = etPassword.text.toString()
        val confirmPassword = etConfirmPassword.text.toString()
        signupLoadingContainer.visibility = View.VISIBLE
        try {
            viewModel!!.register(email, password, confirmPassword)
        } catch (e: Exception) {
            signupLoadingContainer.visibility = View.GONE
            Toast.makeText(this.applicationContext, e.message, Toast.LENGTH_LONG).show()
        }
        viewModel!!.signUpMsg.observe(this, Observer { result ->
            signupLoadingContainer.visibility = View.GONE
            Toast.makeText(this.applicationContext, result.msg, Toast.LENGTH_LONG).show()
            if(result.status) finish()
        })
    }
}