package studios.eaemenkk.overtracker.view.activity

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_feed.*
import studios.eaemenkk.overtracker.R
import studios.eaemenkk.overtracker.view.adapter.CardAdapter
import studios.eaemenkk.overtracker.viewmodel.CardViewModel
import java.lang.Exception

class FeedActivity: AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {
    private var loadingAnimation = AnimationDrawable()
    private val viewModel: CardViewModel by lazy {
        ViewModelProvider(this).get(CardViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed)

        val navigation = findViewById<BottomNavigationView>(R.id.bnvFeed)
        navigation.selectedItemId = R.id.btGlobal
        navigation.setOnNavigationItemSelectedListener { menuItem ->
            when(menuItem.itemId) {
                R.id.btLocal -> {
                    startActivity(Intent(this, LocalFeedActivity::class.java))
                    overridePendingTransition(0, 0)
                }
                R.id.btFollowing -> {
                    startActivity(Intent(this, FollowingActivity::class.java))
                    overridePendingTransition(0, 0)
                }
            }
            return@setOnNavigationItemSelectedListener false
        }

        srlFeed.setOnRefreshListener(this)
        val loadingImage = findViewById<ImageView>(R.id.ivLoading)
        loadingImage.setBackgroundResource(R.drawable.animation)
        loadingAnimation = loadingImage.background as AnimationDrawable
        loadingAnimation.start()
        configureRecyclerView()
        getFeed()
    }

    private fun getFeed() {
        viewModel.cardList.observe(this, Observer { cards ->
            srlFeed.isRefreshing = false
            feedLoadingContainer.visibility = View.GONE
            val adapter = CardAdapter(cards, this)
            rvFeed.adapter = adapter
        })
        viewModel.error.observe(this, Observer { response ->
            if(!response.status) {
                srlFeed.isRefreshing = false
                feedLoadingContainer.visibility = View.GONE
                Toast.makeText(this, response.msg, Toast.LENGTH_SHORT).show()
            }
        })
        feedLoadingContainer.visibility = View.VISIBLE
        viewModel.getFeed()
    }

    private fun configureRecyclerView() {
        rvFeed.layoutManager = LinearLayoutManager(this)
    }

    override fun onBackPressed() {
        finish()
        overridePendingTransition(0, 0)
    }

    override fun onRefresh() {
        getFeed()
    }
}