package studios.eaemenkk.overtracker.view.activity

import android.app.Application
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.Window
import android.view.WindowManager
import com.google.firebase.auth.FirebaseAuth
import studios.eaemenkk.overtracker.R

class SplashActivity : AppCompatActivity() {

    private val mAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!isTaskRoot) {
            finish()
            return
        }
        window.requestFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_splash)
        val handler = Handler()
        handler.postDelayed({
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        },2000)
        mAuth.currentUser?.getIdToken(true)?.addOnCompleteListener { task ->
            if(task.isSuccessful) {
                startActivity(Intent(this, FeedActivity::class.java))
                finish()
                handler.removeCallbacksAndMessages(null)
            }
        }
    }
}
