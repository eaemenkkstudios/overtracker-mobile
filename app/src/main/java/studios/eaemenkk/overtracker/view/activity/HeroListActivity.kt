package studios.eaemenkk.overtracker.view.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_hero_list.*
import kotlinx.android.synthetic.main.activity_hero_list.bnvFeed
import kotlinx.android.synthetic.main.hero_list_item.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import studios.eaemenkk.overtracker.R
import studios.eaemenkk.overtracker.viewmodel.HeroViewModel

class HeroListActivity: AppCompatActivity() {
    private val viewModel: HeroViewModel by lazy {
        ViewModelProvider(this).get(HeroViewModel::class.java)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hero_list)

        bnvFeed.selectedItemId = R.id.btHeroes
        bnvFeed.setOnNavigationItemSelectedListener { menuItem ->
            when(menuItem.itemId) {
                R.id.btGlobal -> {
                    startActivity(Intent("OVERTRACKER_GLOBAL_FEED")
                        .addCategory("OVERTRACKER_GLOBAL_FEED")
                        .addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT))
                    overridePendingTransition(0, 0)
                }
                R.id.btLocal -> {
                    startActivity(Intent("OVERTRACKER_LOCAL_FEED")
                        .addCategory("OVERTRACKER_LOCAL_FEED")
                        .addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT))
                    overridePendingTransition(0, 0)
                }
                R.id.btChat -> {
                    startActivity(Intent("OVERTRACKER_CHAT")
                        .addCategory("OVERTRACKER_CHAT")
                        .addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT))
                    overridePendingTransition(0, 0)
                }
            }
            return@setOnNavigationItemSelectedListener false
        }
        val handler = Handler(this.mainLooper)
        viewModel.heroList.observe(this, Observer { heroes ->
            val density = resources.displayMetrics.density
            val dpWidth = resources.displayMetrics.widthPixels / density
            glHeroList.columnCount = (dpWidth / 96).toInt()
            glHeroList.removeAllViews()
            heroes.forEach { hero ->
                val view = layoutInflater.inflate(R.layout.hero_list_item, glHeroList, false)
                Picasso.get().load(hero.img).into(view.ivHeroPortrait)
                view.tvHeroName.text = hero.raw_name
                view.ivHeroRole.setImageResource(
                    when(hero.role) {
                        "support" -> R.drawable.support
                        "damage" -> R.drawable.damage
                        "tank" -> R.drawable.tank
                        else -> R.drawable.unknown
                    }
                )
                view.setOnClickListener { handler.post { showHeroDetails(hero.name) } }
                glHeroList.addView(view)
            }
            heroListLoadingContainer.visibility = View.GONE
        })

        heroListLoadingContainer.visibility = View.VISIBLE
        getHeroes()
    }

    override fun onBackPressed() {
        finish()
        overridePendingTransition(0, 0)
    }

    override fun onResume() {
        super.onResume()
        overridePendingTransition(0, 0)
    }

    private fun getHeroes() {
        GlobalScope.launch {
            viewModel.getHeroes()
        }
    }

    private fun showHeroDetails(heroName: String) {
        val intent = Intent("OVERTRACKER_HERO_INFO")
            .addCategory("OVERTRACKER_HERO_INFO")
        intent.putExtra("heroName", heroName)
        startActivity(intent)
    }
}