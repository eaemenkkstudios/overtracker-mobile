package studios.eaemenkk.overtracker.view.activity

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_feed.*
import kotlinx.android.synthetic.main.activity_info.*
import kotlinx.android.synthetic.main.activity_sign_up.*
import studios.eaemenkk.overtracker.R
import studios.eaemenkk.overtracker.view.adapter.PlayerInfoAdapter
import studios.eaemenkk.overtracker.viewmodel.PlayerViewModel

class InfoActivity: AppCompatActivity() {
    private val mAuth = FirebaseAuth.getInstance()
    private val viewModel: PlayerViewModel by lazy {
        ViewModelProvider(this).get(PlayerViewModel::class.java)
    }
    private var loadingAnimation = AnimationDrawable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)
        val loadingImage = findViewById<ImageView>(R.id.ivLoading)
        loadingImage.setBackgroundResource(R.drawable.animation)
        loadingAnimation = loadingImage.background as AnimationDrawable
        loadingAnimation.start()
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
                    val adapter = PlayerInfoAdapter(player.scores)
                    rvInfoScores.adapter = adapter
                }
                tvInfoTag.text = player.tag
                tvInfoTagNum.text = player.tagNum
                tvInfoCurrentDamage.text = player.now?.rank?.damage?.sr
                tvInfoCurrentSupport.text = player.now?.rank?.support?.sr
                tvInfoCurrentTank.text = player.now?.rank?.tank?.sr
                tvInfoMain.text = player.now?.main?.hero
                tvInfoPlatform.text = player.platform
                Picasso.get().load(player.now?.portrait).into(ivInfoMain)
                Picasso.get().load(player.portrait).into(ivInfoPortrait)
                ivInfoEndorsement.setImageResource(
                    when(player.now?.endorsement){
                        "1" -> R.drawable.endorsement_1
                        "2" -> R.drawable.endorsement_2
                        "3" -> R.drawable.endorsement_3
                        "4" -> R.drawable.endorsement_4
                        "5" -> R.drawable.endorsement_5
                        else -> R.drawable.unknown
                    });
                infoLoadingContainer.visibility = View.GONE
            })

            val playerId = intent.getStringExtra("playerId")
            if(playerId != null) {
                val operation = mAuth.currentUser?.getIdToken(true)
                operation?.addOnCompleteListener {task ->
                    if(task.isSuccessful) {
                        viewModel.playerInfo(task.result?.token.toString(), playerId)
                    }
                }
            }
        }

    }

}