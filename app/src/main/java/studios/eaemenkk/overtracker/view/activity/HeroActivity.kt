package studios.eaemenkk.overtracker.view.activity

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.AnimationDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_hero.*
import kotlinx.android.synthetic.main.activity_hero.adView
import kotlinx.android.synthetic.main.activity_hero.ivBack
import kotlinx.android.synthetic.main.activity_info.*
import studios.eaemenkk.overtracker.R
import studios.eaemenkk.overtracker.viewmodel.HeroViewModel

class HeroActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private val viewModel: HeroViewModel by lazy {
        ViewModelProvider(this).get(HeroViewModel::class.java)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hero)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        transparent_image.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    svHero.requestDisallowInterceptTouchEvent(true)
                    false
                }

                MotionEvent.ACTION_UP -> {
                    svHero.requestDisallowInterceptTouchEvent(false)
                    true;
                }

                MotionEvent.ACTION_MOVE-> {
                    svHero.requestDisallowInterceptTouchEvent(true)
                    false
                }
                else -> true
            }
        }
        ivBack.setOnClickListener{
            finish()
        }

        heroLoadingContainer.visibility = View.VISIBLE
        adView.loadAd(AdRequest.Builder().build())
        getHeroInfo()
        showInfo()
    }

    private fun getHeroInfo() {
        if(intent.hasExtra("heroName")) {
            val heroName = intent.getStringExtra("heroName") ?: return
            viewModel.getHero(heroName)
        }
    }

    private fun showInfo() {
        viewModel.heroInfo.observe(this, Observer { hero ->
            tvHeroName.text = hero.friendlyName
            Picasso.get().load(hero.img).into(ivHeroImg)

            vvIdle.setVideoURI(Uri.parse(hero.video))
            vvIdle.setOnPreparedListener { m ->
                m.setVolume(0f, 0f)
                m.isLooping = true
                m.start()
                vvIdle.postDelayed({
                    ivHeroImg.visibility = View.INVISIBLE
                }, 150)
            }

            vvIdle.setOnErrorListener { _, _, _ ->
                vvIdle.visibility = View.INVISIBLE
                ivHeroImg.visibility = View.VISIBLE
                true
            }

            ivHeroRole.setImageResource(
                when(hero.role) {
                    "support " -> R.drawable.support
                    "damage " -> R.drawable.damage
                    "tank " -> R.drawable.tank
                    else -> R.drawable.unknown
                }
            )
            tvHeroRole.text = hero.role
            tvHeroLore.text = hero.lore
            ivStar1.setColorFilter(getColor(R.color.colorDetail))
            ivStar2.setColorFilter(getColor(R.color.colorDetail))
            ivStar3.setColorFilter(getColor(R.color.colorDetail))
            if(hero.difficulty != null) {
                when (hero.difficulty) {
                    1 -> {
                        ivStar1.setColorFilter(getColor(R.color.colorPrimary))
                    }
                    2 -> {
                        ivStar1.setColorFilter(getColor(R.color.colorPrimary))
                        ivStar2.setColorFilter(getColor(R.color.colorPrimary))
                    }
                    3 -> {
                        ivStar1.setColorFilter(getColor(R.color.colorPrimary))
                        ivStar2.setColorFilter(getColor(R.color.colorPrimary))
                        ivStar3.setColorFilter(getColor(R.color.colorPrimary))
                    }
                }
            }
            heroLoadingContainer.visibility = View.GONE
        })
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}
