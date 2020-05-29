package studios.eaemenkk.overtracker.view.activity

import android.content.res.ColorStateList
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.ads.AdRequest
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_info.*
import kotlinx.android.synthetic.main.activity_info.adView
import studios.eaemenkk.overtracker.R
import studios.eaemenkk.overtracker.R.*
import studios.eaemenkk.overtracker.view.adapter.PlayerScoreAdapter
import studios.eaemenkk.overtracker.viewmodel.PlayerViewModel
import java.lang.Exception

class InfoActivity: AppCompatActivity() {
    private val viewModel: PlayerViewModel by lazy {
        ViewModelProvider(this).get(PlayerViewModel::class.java)
    }
    private var loadingAnimation = AnimationDrawable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_info)
        val loadingImage = findViewById<ImageView>(id.ivLoading)
        loadingImage.setBackgroundResource(drawable.animation)
        loadingAnimation = loadingImage.background as AnimationDrawable
        loadingAnimation.start()

        adView.loadAd(AdRequest.Builder().build())
        configureRecyclerView()
        getPlayerDetails()
    }

    private fun configureRecyclerView() {
        rvInfoScores.layoutManager = LinearLayoutManager(this)
    }

    private fun getPlayerDetails() {
        infoLoadingContainer.visibility = View.VISIBLE
        if(intent.hasExtra("playerId")) {
            viewModel.playerDetails.observe(this, Observer { player ->
                if(player.scores != null) {
                    val adapter = PlayerScoreAdapter(player.scores)
                    rvInfoScores.adapter = adapter
                }
                tvInfoMainTime.text = player.now?.main?.time
                tvInfoTag.text = player.tag
                tvInfoTagNum.text = player.tagNum
                ivInfoMainRole.setImageResource(when (player.now?.main?.role) {
                    "support" -> drawable.support
                    "damage" -> drawable.damage
                    "tank" -> drawable.tank
                    else -> drawable.unknown
                })
                tvInfoCurrentDamage.text = player.now?.rank?.damage?.sr.toString()
                tvInfoCurrentSupport.text = player.now?.rank?.support?.sr.toString()
                tvInfoCurrentTank.text = player.now?.rank?.tank?.sr.toString()
                tvInfoMain.text = player.now?.main?.hero
                tvInfoPlatform.text = player.platform
                Picasso.get().load(player.now?.portrait).into(ivInfoMain)
                Picasso.get().load(player.portrait).into(ivInfoPortrait)
                ivInfoEndorsement.setImageResource(
                    when(player.now?.endorsement){
                        "1" -> drawable.endorsement_1
                        "2" -> drawable.endorsement_2
                        "3" -> drawable.endorsement_3
                        "4" -> drawable.endorsement_4
                        "5" -> drawable.endorsement_5
                        else -> drawable.unknown
                    });
                infoLoadingContainer.visibility = View.GONE
            })

            val playerId = intent.getStringExtra("playerId") ?: return

            viewModel.followed.observe(this, Observer { result ->
                if(result) {
                    btFollow.text = "Unfollow"
                    btFollow.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorDetail))
                    btFollow.setOnClickListener { viewModel.unfollowPlayer(playerId) }
                }
            })

            viewModel.unfollowed.observe(this, Observer { result ->
                if(result) {
                    btFollow.text = "Follow"
                    btFollow.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorPrimary))
                    btFollow.setOnClickListener { viewModel.followPlayer(playerId) }
                }
            })
            viewModel.isFollowing(playerId)
            try {
                viewModel.playerInfo(playerId)
            } catch (e: Exception) {
                infoLoadingContainer.visibility = View.GONE
                Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}