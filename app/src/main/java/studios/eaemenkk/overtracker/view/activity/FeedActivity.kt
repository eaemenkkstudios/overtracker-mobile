package studios.eaemenkk.overtracker.view.activity

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.AdRequest
import kotlinx.android.synthetic.main.activity_feed.*
import studios.eaemenkk.overtracker.R
import studios.eaemenkk.overtracker.view.adapter.CardAdapter
import studios.eaemenkk.overtracker.viewmodel.CardViewModel

class FeedActivity: AppCompatActivity() {
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
        setContentView(R.layout.activity_feed)

        bnvFeed.selectedItemId = R.id.btGlobal
        bnvFeed.setOnNavigationItemSelectedListener { menuItem ->
            when(menuItem.itemId) {
                R.id.btLocal -> {
                    val intent = Intent(this, LocalFeedActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                    startActivity(intent)
                    overridePendingTransition(0, 0)
                }
                R.id.btFollowing -> {
                    val intent = Intent(this, FollowingActivity::class.java)
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

        srlFeed.setOnRefreshListener { onRefresh() }
        srlFeed.setColorSchemeColors(ContextCompat.getColor(this, R.color.colorPrimary))
        srlFeed.setProgressBackgroundColorSchemeColor(ContextCompat.getColor(this, R.color.colorPrimaryDark))
        rvFeed.adapter = adapter

        ivLoading.setBackgroundResource(R.drawable.animation)
        (ivLoading.background as AnimationDrawable).start()

        configureRecyclerView()
        getFeed()
    }

    private fun getFeed() {
        if(showLoadingIcon) feedLoadingContainer.visibility = View.VISIBLE
        isLoading = true
        viewModel.getFeed(page)
    }

    private fun configureRecyclerView() {
        viewModel.cardList.observe(this, Observer { cards ->
            srlFeed.isRefreshing = false
            feedLoadingContainer.visibility = View.GONE
            isLoading = cards.isNullOrEmpty()
            if(refresh) adapter.setCards(cards)
            else adapter.addCards(cards)
        })

        viewModel.error.observe(this, Observer { response ->
            if(!response.status && response.msg != "") {
                srlFeed.isRefreshing = false
                feedLoadingContainer.visibility = View.GONE
                Toast.makeText(this, response.msg, Toast.LENGTH_SHORT).show()
            }
        })

        rvFeed.layoutManager = layoutManager
        rvFeed.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if(dy > 0) {
                    val visibleItemCount = layoutManager.childCount
                    val pastVisibleItem = layoutManager.findFirstCompletelyVisibleItemPosition()
                    val total = adapter.itemCount

                    if(!isLoading && visibleItemCount + pastVisibleItem >= total) {
                        rvFeed.post { getNextPage() }
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
        getFeed()
    }

    fun getNextPage() {
        page++
        refresh = false
        showLoadingIcon = true
        getFeed()
    }
}