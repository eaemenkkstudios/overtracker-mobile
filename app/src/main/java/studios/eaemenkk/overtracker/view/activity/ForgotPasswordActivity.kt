package studios.eaemenkk.overtracker.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_forgot_password.*
import studios.eaemenkk.overtracker.R
import studios.eaemenkk.overtracker.viewmodel.AuthViewModel
import java.lang.Exception

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var viewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        viewModel = ViewModelProvider(this).get(AuthViewModel::class.java)

        viewModel.forgotPasswordMsg.observe(this, Observer { result ->
            if(result.msg != "") {
                Toast.makeText(this.applicationContext, result.msg, Toast.LENGTH_SHORT).show()
                if(result.status) finish()
            }
        })

        btSendEmail.setOnClickListener { sendEmail() }
    }

    fun sendEmail() {
        val email = etEmail.text.toString()
        try {
            viewModel.forgotPassword(email)
        } catch (e: Exception) {
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
        }


    }
}
