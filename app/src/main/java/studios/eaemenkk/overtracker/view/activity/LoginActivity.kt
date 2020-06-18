package studios.eaemenkk.overtracker.view.activity

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.PopupWindow
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.findFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.google.android.gms.ads.AdRequest
import kotlinx.android.synthetic.main.activity_login.*
import studios.eaemenkk.overtracker.R
import studios.eaemenkk.overtracker.view.adapter.PagerAdapter
import studios.eaemenkk.overtracker.view.fragment.DialogFragmentWindow
import studios.eaemenkk.overtracker.viewmodel.AuthViewModel


class LoginActivity : AppCompatActivity() {

    private val viewModel: AuthViewModel by lazy {
        ViewModelProvider(this).get(AuthViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        viewModel.loginMsg.observe(this, Observer { result ->
            if (result.status) {
                loginLoadingContainer.visibility = View.VISIBLE
                viewModel.isAuth()
            } else {
                viewModel.logout()
                Toast.makeText(this, getString(R.string.login_failed), Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.isAuthMsg.observe(this, Observer { result ->
            if(result.msg != "") {
                Toast.makeText(this, result.msg, Toast.LENGTH_SHORT).show()
                if(result.status) {
                    val intent = Intent("OVERTRACKER_GLOBAL_FEED")
                        .addCategory("OVERTRACKER_GLOBAL_FEED")
                    startActivity(intent)
                    finish()
                } else loginLoadingContainer.visibility = View.GONE
            }
        })

        if(intent.action == Intent.ACTION_VIEW) {
            val session = intent.data?.getQueryParameter("session")
            if(!session.isNullOrEmpty()) {
                viewModel.login(session)
            }
        }

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        adView.loadAd(AdRequest.Builder().build())
        btSignIn.setOnClickListener { bnetSignIn() }
    }

    private fun bnetSignIn() {
        val openURL = Intent(Intent.ACTION_VIEW)
        openURL.data = Uri.parse("${getString(R.string.api_base_url)}/auth/bnet")
        startActivity(openURL)
    }
}