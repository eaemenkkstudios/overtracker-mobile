package studios.eaemenkk.overtracker.view.activity

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_feed_local.*
import studios.eaemenkk.overtracker.R
import studios.eaemenkk.overtracker.view.adapter.CardAdapter
import studios.eaemenkk.overtracker.viewmodel.CardViewModel

class LocalFeedActivity: AppCompatActivity() {
    private val mAuth = FirebaseAuth.getInstance()
    private var loadingAnimation = AnimationDrawable()
    private val viewModel: CardViewModel by lazy {
        ViewModelProvider(this).get(CardViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed_local)

        val navigation = findViewById<BottomNavigationView>(R.id.bnvFeed)
        navigation.selectedItemId = R.id.btLocal
        navigation.setOnNavigationItemSelectedListener { menuItem ->
            when(menuItem.itemId) {
                R.id.btGlobal -> {
                    startActivity(Intent(this, FeedActivity::class.java))
                    overridePendingTransition(0, 0)
                }
                R.id.btFollowing -> {
                    startActivity(Intent(this, FollowingActivity::class.java))
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
        getLocalFeed()
    }

    private fun getLocalFeed() {
        viewModel.localCardList.observe(this, Observer { cards ->
            feedLocalLoadingContainer.visibility = View.GONE
            val adapter = CardAdapter(cards, this)
            rvFeedLocal.adapter = adapter
        })
        feedLocalLoadingContainer.visibility = View.VISIBLE
        val operation = mAuth.currentUser?.getIdToken(true)
        operation?.addOnCompleteListener { task ->
            if(task.isSuccessful) {
                viewModel.getLocalFeed(task.result?.token.toString())
            }
        }
    }

    private fun configureRecyclerView() {
        rvFeedLocal.layoutManager = LinearLayoutManager(this)
    }

    override fun onBackPressed() {
        finish()
        overridePendingTransition(0, 0)
    }
}