package studios.eaemenkk.overtracker.view.activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.Window
import android.view.WindowManager
import com.google.android.gms.ads.MobileAds
import studios.eaemenkk.overtracker.R

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MobileAds.initialize(this)
        if (!isTaskRoot) {
            finish()
            return
        }
        window.requestFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_splash)
        val handler = Handler()
        handler.postDelayed({
            val uri = Uri.parse("overtracker://login")
            val intent = Intent("LOGIN").addCategory("LOGIN")
            intent.data = uri
            startActivity(intent)
            finish()
        },2000)
        val sharedPreferences = getSharedPreferences(packageName, MODE_PRIVATE)
        val session = sharedPreferences.getString("session", null)
        if(session != null) {
            val intent = Intent("GLOBAL_FEED")
                .addCategory("GLOBAL_FEED")
            startActivity(intent)
            finish()
            handler.removeCallbacksAndMessages(null)
        }
    }
}
