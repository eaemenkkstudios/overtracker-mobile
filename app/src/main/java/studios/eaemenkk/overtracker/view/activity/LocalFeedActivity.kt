package studios.eaemenkk.overtracker.view.activity

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.PopupWindow
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_feed_local.*
import kotlinx.android.synthetic.main.activity_feed_local.bnvFeed
import kotlinx.android.synthetic.main.activity_following.*
import studios.eaemenkk.overtracker.R
import studios.eaemenkk.overtracker.view.adapter.CardAdapter
import studios.eaemenkk.overtracker.viewmodel.CardViewModel
import studios.eaemenkk.overtracker.viewmodel.PlayerViewModel

class LocalFeedActivity: AppCompatActivity() {
    private var page = 1
    private var refresh = true
    private var isLoading = false
    private var showLoadingIcon = true
    private lateinit var popupWindow: PopupWindow
    private lateinit var loadingContainer: ConstraintLayout
    private var tag: String = ""
    private var platform: String = "pc"
    private val layoutManager = LinearLayoutManager(this)
    private val adapter = CardAdapter(this, supportFragmentManager)
    private val cardViewModel: CardViewModel by lazy {
        ViewModelProvider(this).get(CardViewModel::class.java)
    }
    private val playerViewModel: PlayerViewModel by lazy {
        ViewModelProvider(this).get(PlayerViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed_local)

        bnvFeed.selectedItemId = R.id.btLocal
        bnvFeed.setOnNavigationItemSelectedListener { menuItem ->
            when(menuItem.itemId) {
                R.id.btGlobal -> {
                    startActivity(Intent("OVERTRACKER_GLOBAL_FEED")
                        .addCategory("OVERTRACKER_GLOBAL_FEED")
                        .addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT))
                    overridePendingTransition(0, 0)
                }
                R.id.btProfile -> {
                    startActivity(Intent("OVERTRACKER_PROFILE")
                        .addCategory("OVERTRACKER_PROFILE")
                        .addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT))
                    overridePendingTransition(0, 0)
                }
                R.id.btHeroes -> {
                    startActivity(Intent("OVERTRACKER_HERO_LIST")
                        .addCategory("OVERTRACKER_HERO_LIST")
                        .addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT))
                    overridePendingTransition(0, 0)
                }
                R.id.btChat -> {
                    startActivity(Intent("OVERTRACKER_CHAT")
                        .addCategory("OVERTRACKER_CHAT")
                        .addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT))
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

        playerViewModel.created.observe(this, Observer { result ->
            loadingContainer.visibility = View.GONE
            popupWindow.dismiss()
            Toast.makeText(this, result.msg, Toast.LENGTH_SHORT).show()
        })

        abFollow.setOnClickListener { popup() }
        abFollowed.setOnClickListener { startActivity(Intent("OVERTRACKER_FOLLOWED_PLAYERS").addCategory("OVERTRACKER_FOLLOWED_PLAYERS")) }

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        configureRecyclerView()
        getLocalFeed()
    }

    private fun getLocalFeed() {
        if(showLoadingIcon) feedLocalLoadingContainer.visibility = View.VISIBLE
        isLoading = true
        cardViewModel.getLocalFeed(page)
    }

    private fun configureRecyclerView() {
        cardViewModel.localCardList.observe(this, Observer { cards ->
            feedLocalLoadingContainer.visibility = View.GONE
            srlFeedLocal.isRefreshing = false
            isLoading = cards.isNullOrEmpty()
            if(refresh) adapter.setCards(cards)
            else adapter.addCards(cards)
        })

        cardViewModel.error.observe(this, Observer { response ->
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

    private fun popup() {
        popupWindow = PopupWindow(this)
        val view = layoutInflater.inflate(R.layout.follow_popup, null)
        popupWindow.contentView = view
        popupWindow.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val btnAdd = view.findViewById<Button>(R.id.btAddBattletag)
        loadingContainer = view.findViewById(R.id.followLoadingContainer)
        val etTag = view.findViewById<EditText>(R.id.etBattletag)
        btnAdd.setOnClickListener {
            tag = etTag.text.toString()
            createPlayer()
        }
        clLocalFeed.setOnClickListener { popupWindow.dismiss() }
        popupWindow.isFocusable = true
        popupWindow.isTouchable = true
        popupWindow.showAtLocation(clLocalFeed, Gravity.CENTER, 0, 0)
    }

    private fun createPlayer() {
        loadingContainer.visibility = View.VISIBLE
        try {
            playerViewModel.createPlayer(tag, platform)
        } catch (e: Exception) {
            loadingContainer.visibility = View.GONE
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
        }
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