package studios.eaemenkk.overtracker.view.activity

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_following.*
import studios.eaemenkk.overtracker.R
import studios.eaemenkk.overtracker.view.adapter.PlayerAdapter
import studios.eaemenkk.overtracker.viewmodel.PlayerViewModel

class FollowingActivity : AppCompatActivity() {

    private val mAuth = FirebaseAuth.getInstance()
    private var loadingAnimation = AnimationDrawable()
    private val viewModel: PlayerViewModel by lazy {
        ViewModelProvider(this).get(PlayerViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_following)
        val navigation = findViewById<BottomNavigationView>(R.id.bnvFeed)
        navigation.selectedItemId = R.id.btFollowing
        navigation.setOnNavigationItemSelectedListener { menuItem ->
            when(menuItem.itemId) {
                R.id.btGlobal -> {
                    startActivity(Intent(this, FeedActivity::class.java))
                    overridePendingTransition(0, 0)
                }
                R.id.btLocal -> {
                    startActivity(Intent(this, LocalFeedActivity::class.java))
                    overridePendingTransition(0, 0)
                }
            }
            return@setOnNavigationItemSelectedListener false
        }
        val loadingImage = findViewById<ImageView>(R.id.ivLoading)
        loadingImage.setBackgroundResource(R.drawable.animation)
        loadingAnimation = loadingImage.background as AnimationDrawable
        loadingAnimation.start()
        configureRecyclerView()
        showPlayers()
    }

    private fun configureRecyclerView() {
        rvFollowing.layoutManager = LinearLayoutManager(this)
    }

    private fun showPlayers() {
        followingLoadingContainer.visibility = View.VISIBLE
        viewModel.playerList.observe(this, Observer { players ->
            followingLoadingContainer.visibility = View.GONE
            val adapter = PlayerAdapter(players)
            rvFollowing.adapter = adapter
        })
        val operation = mAuth.currentUser?.getIdToken(true)
        operation?.addOnCompleteListener {task ->
            if(task.isSuccessful) {
                viewModel.followedPlayers(task.result?.token.toString())
            }
        }
    }

    override fun onBackPressed() {
        finish()
        overridePendingTransition(0, 0)
    }
}
