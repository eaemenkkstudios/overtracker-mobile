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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.google.android.gms.ads.AdRequest
import kotlinx.android.synthetic.main.activity_login.*
import studios.eaemenkk.overtracker.R
import studios.eaemenkk.overtracker.view.adapter.PagerAdapter
import studios.eaemenkk.overtracker.viewmodel.AuthViewModel


class LoginActivity : AppCompatActivity() {

    private val viewModel: AuthViewModel by lazy {
        ViewModelProvider(this).get(AuthViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        viewModel.loginMsg.observe(this, Observer { result ->
            if(result.msg != "") {
                Toast.makeText(this.applicationContext, result.msg, Toast.LENGTH_SHORT).show()
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

//        button.setOnClickListener { startActivity(Intent(this, toolbarTest::class.java)) }
        button.setOnClickListener { popup() }
    }

    private fun bnetSignIn() {
        val openURL = Intent(Intent.ACTION_VIEW)
        openURL.data = Uri.parse("${getString(R.string.api_base_url)}/auth/bnet")
        startActivity(openURL)
    }

    private lateinit var popupWindow: PopupWindow
    private fun popup() {
        popupWindow = PopupWindow(this)
        val view = layoutInflater.inflate(R.layout.profile_popup, null)
        popupWindow.contentView = view
        popupWindow.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val pagerAdapter = PagerAdapter(supportFragmentManager,2)
        val viewPager = view.findViewById<ViewPager>(R.id.vpProfile)
        viewPager.adapter = pagerAdapter

        clLogin.setOnClickListener { popupWindow.dismiss() }
        popupWindow.isFocusable = true
        popupWindow.isTouchable = true
        popupWindow.showAtLocation(clLogin, Gravity.CENTER, 0, 0)
    }
}