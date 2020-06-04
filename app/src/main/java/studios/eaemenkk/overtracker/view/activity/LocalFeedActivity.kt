package studios.eaemenkk.overtracker.view.activity

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_feed_local.*
import studios.eaemenkk.overtracker.R
import studios.eaemenkk.overtracker.view.adapter.CardAdapter
import studios.eaemenkk.overtracker.viewmodel.CardViewModel

class LocalFeedActivity: AppCompatActivity() {
    private var page = 1
    private var refresh = true
    private var isLoading = false
    private var showLoadingIcon = true
    private val layoutManager = LinearLayoutManager(this)
    private val adapter = CardAdapter(this)
    private val viewModel: CardViewModel by lazy {
        ViewModelProvider(this).get(CardViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed_local)

        bnvFeed.selectedItemId = R.id.btLocal
        bnvFeed.setOnNavigationItemSelectedListener { menuItem ->
            when(menuItem.itemId) {
                R.id.btGlobal -> {
                    val intent = Intent("OVERTRACKER_GLOBAL_FEED")
                        .addCategory("OVERTRACKER_GLOBAL_FEED")
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                    startActivity(intent)
                    overridePendingTransition(0, 0)
                }
                R.id.btFollowing -> {
                    val intent = Intent("OVERTRACKER_FOLLOWED_PLAYERS")
                        .addCategory("OVERTRACKER_FOLLOWED_PLAYERS")
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
                else -> {
                    val smoothScroller: RecyclerView.SmoothScroller = object: LinearSmoothScroller(this) {
                        override fun getVerticalSnapPreference(): Int {
                            return SNAP_TO_START
                        }
                    }
                    smoothScroller.targetPosition = 0
                    layoutManager.startSmoothScroll(smoothScroller)
                }
            }
            return@setOnNavigationItemSelectedListener false
        }

        srlFeedLocal.setOnRefreshListener { onRefresh() }
        srlFeedLocal.setColorSchemeColors(ContextCompat.getColor(this, R.color.colorPrimary))
        srlFeedLocal.setProgressBackgroundColorSchemeColor(ContextCompat.getColor(this, R.color.colorPrimaryDark))
        rvFeedLocal.adapter = adapter

        ivLoading.setBackgroundResource(R.drawable.animation)
        (ivLoading.background as AnimationDrawable).start()

        configureRecyclerView()
        getLocalFeed()
    }

    private fun getLocalFeed() {
        if(showLoadingIcon) feedLocalLoadingContainer.visibility = View.VISIBLE
        isLoading = true
        viewModel.getLocalFeed(page)
    }

    private fun configureRecyclerView() {
        viewModel.localCardList.observe(this, Observer { cards ->
            feedLocalLoadingContainer.visibility = View.GONE
            srlFeedLocal.isRefreshing = false
            isLoading = cards.isNullOrEmpty()
            if(refresh) adapter.setCards(cards)
            else adapter.addCards(cards)
        })

        viewModel.error.observe(this, Observer { response ->
            if(!response.status && response.msg != "") {
                feedLocalLoadingContainer.visibility = View.GONE
                Toast.makeText(this, response.msg, Toast.LENGTH_SHORT).show()
            }
        })

        rvFeedLocal.layoutManager = layoutManager
        rvFeedLocal.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if(dy > 0) {
                    val visibleItemCount = layoutManager.childCount
                    val pastVisibleItem = layoutManager.findFirstCompletelyVisibleItemPosition()
                    val total = adapter.itemCount

                    if(!isLoading && visibleItemCount + pastVisibleItem >= total) {
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

    override fun onResume() {
        super.onResume()
        overridePendingTransition(0, 0)
    }

    private fun onRefresh() {
        page = 1
        refresh = true
        showLoadingIcon = false
        getLocalFeed()
    }

    fun getNextPage(){
        page++
        refresh = false
        showLoadingIcon = true
        getLocalFeed()
    }
}