package studios.eaemenkk.overtracker.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_profile.*
import studios.eaemenkk.overtracker.R

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        // bnvFeed.selectedItemId = R.id.btProfile
        bnvFeed.setOnNavigationItemSelectedListener { menuItem ->
            when(menuItem.itemId) {
                R.id.btGlobal -> {
                    val intent = Intent("OVERTRACKER_GLOBAL_FEED")
                        .addCategory("OVERTRACKER_GLOBAL_FEED")
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                    startActivity(intent)
                    overridePendingTransition(0, 0)
                }
                R.id.btLocal -> {
                    val intent = Intent("OVERTRACKER_LOCAL_FEED")
                        .addCategory("OVERTRACKER_LOCAL_FEED")
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                    startActivity(intent)
                    overridePendingTransition(0, 0)
                }
                R.id.btHeroes -> {
                    val intent = Intent("OVERTRACKER_HERO_LIST")
                        .addCategory("OVERTRACKER_HERO_LIST")
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                    startActivity(intent)
                    overridePendingTransition(0, 0)
                }
                R.id.btChat -> {
                    val intent = Intent("OVERTRACKER_CHAT")
                        .addCategory("OVERTRACKER_CHAT")
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                    startActivity(intent)
                    overridePendingTransition(0, 0)
                }
            }
            return@setOnNavigationItemSelectedListener false
        }
    }

    override fun onBackPressed() {
        finish()
        overridePendingTransition(0, 0)
    }

    override fun onResume() {
        super.onResume()
        overridePendingTransition(0, 0)
    }
}