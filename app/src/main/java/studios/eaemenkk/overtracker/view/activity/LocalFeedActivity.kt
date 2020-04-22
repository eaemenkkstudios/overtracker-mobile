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
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_feed_local.*
import studios.eaemenkk.overtracker.R
import studios.eaemenkk.overtracker.view.adapter.CardAdapter
import studios.eaemenkk.overtracker.viewmodel.CardViewModel
import java.lang.Exception

class LocalFeedActivity: AppCompatActivity() {
    private var page = 1
    private var refresh = true
    private var isLoading = false
    private val layoutManager = LinearLayoutManager(this)
    private val adapter = CardAdapter(this)
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
                else -> {
                    layoutManager.smoothScrollToPosition(rvFeedLocal, object: RecyclerView.State() {}, 0)
                }
            }
            return@setOnNavigationItemSelectedListener false
        }

        srlFeedLocal.setOnRefreshListener { onRefresh() }
        rvFeedLocal.adapter = adapter

        viewModel.localCardList.observe(this, Observer { cards ->
            feedLocalLoadingContainer.visibility = View.GONE
            srlFeedLocal.isRefreshing = false
            isLoading = cards.isEmpty()
            if(refresh) adapter.setCards(cards)
            else adapter.addCards(cards)
        })

        viewModel.error.observe(this, Observer { response ->
            if(!response.status && response.msg != "") {
                feedLocalLoadingContainer.visibility = View.GONE
                Toast.makeText(this, response.msg, Toast.LENGTH_SHORT).show()
            }
        })

        val loadingImage = findViewById<ImageView>(R.id.ivLoading)
        loadingImage.setBackgroundResource(R.drawable.animation)
        loadingAnimation = loadingImage.background as AnimationDrawable
        loadingAnimation.start()
        configureRecyclerView()
        getLocalFeed()
    }

    private fun getLocalFeed() {
        feedLocalLoadingContainer.visibility = View.VISIBLE
        val operation = mAuth.currentUser?.getIdToken(true)
        operation?.addOnCompleteListener { task ->
            if(task.isSuccessful) {
                viewModel.getLocalFeed(task.result?.token.toString(), page, refresh)
            } else {
                feedLocalLoadingContainer.visibility = View.GONE
                Toast.makeText(this, "Could not load feed, please try again...", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun configureRecyclerView() {
        rvFeedLocal.layoutManager = LinearLayoutManager(this)
        rvFeedLocal.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if(dy > 0) {
                    val visibleItemCount = layoutManager.childCount
                    val pastVisibleItem = layoutManager.findFirstCompletelyVisibleItemPosition()
                    val total = rvFeedLocal.adapter!!.itemCount

                    if(!isLoading && visibleItemCount + pastVisibleItem >= total) {
                        isLoading = true
                        rvFeedLocal.post { getNextPage() }
                    }
                }
            }
        })
    }

    override fun onBackPressed() {
        finish()
        overridePendingTransition(0, 0)
    }

    private fun onRefresh() {
        page = 1
        refresh = true
        getLocalFeed()
    }

    fun getNextPage(){
        page++
        refresh = false
        getLocalFeed()
    }
}