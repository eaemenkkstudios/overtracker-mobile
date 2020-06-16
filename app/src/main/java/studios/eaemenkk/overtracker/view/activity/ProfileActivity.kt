package studios.eaemenkk.overtracker.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.profile_popup.*
import studios.eaemenkk.overtracker.R
import studios.eaemenkk.overtracker.view.adapter.PagerAdapter

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_popup)

        val pagerAdapter = PagerAdapter(supportFragmentManager, 2)
        vpProfile.adapter = pagerAdapter
    }
}