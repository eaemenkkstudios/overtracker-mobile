package studios.eaemenkk.overtracker.view.activity

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_hero_list.*
import kotlinx.android.synthetic.main.hero_list_item.view.*
import studios.eaemenkk.overtracker.R
import studios.eaemenkk.overtracker.viewmodel.HeroViewModel

class HeroListActivity: AppCompatActivity() {
    private val viewModel: HeroViewModel by lazy {
        ViewModelProvider(this).get(HeroViewModel::class.java)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hero_list)
        viewModel.heroList.observe(this, Observer { heroes ->
            val density = resources.displayMetrics.density
            val dpWidth = resources.displayMetrics.widthPixels / density
            glHeroList.columnCount = (dpWidth / 96).toInt()
            glHeroList.removeAllViews()
            heroes.forEach { hero ->
                val view = LayoutInflater.from(this).inflate(R.layout.hero_list_item, glHeroList, false)
                Picasso.get().load(hero.img).into(view.ivHeroPortrait)
                view.tvHeroName.text = hero.friendlyName
                view.ivHeroRole.setImageResource(
                    when(hero.role) {
                        "support" -> R.drawable.support
                        "damage" -> R.drawable.damage
                        "tank" -> R.drawable.tank
                        else -> R.drawable.unknown
                    }
                )
                view.setOnClickListener { showHeroDetails(hero.name) }
                glHeroList.addView(view)
            }
            heroListLoadingContainer.visibility = View.GONE
        })
        ivLoading.setBackgroundResource(R.drawable.animation)
        (ivLoading.background as AnimationDrawable).start()
        heroListLoadingContainer.visibility = View.VISIBLE
        getHeroes()
    }

    private fun getHeroes() {
        viewModel.getHeroes()
    }

    private fun showHeroDetails(heroName: String) {
        val intent = Intent(this, HeroActivity::class.java)
        intent.putExtra("heroName", heroName)
        startActivity(intent)
    }
}