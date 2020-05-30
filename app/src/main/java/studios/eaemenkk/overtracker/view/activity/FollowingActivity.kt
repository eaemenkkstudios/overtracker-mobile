package studios.eaemenkk.overtracker.view.activity

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.ColorDrawable
import android.net.Uri
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
import kotlinx.android.synthetic.main.activity_following.*
import studios.eaemenkk.overtracker.R
import studios.eaemenkk.overtracker.view.adapter.PlayerAdapter
import studios.eaemenkk.overtracker.viewmodel.AuthViewModel
import studios.eaemenkk.overtracker.viewmodel.PlayerViewModel

class FollowingActivity : AppCompatActivity() {
    private val adapter = PlayerAdapter(this)
    private lateinit var loadingContainer: ConstraintLayout
    private lateinit var popupWindow: PopupWindow
    private var showLoadingIcon = true
    private var isLoading = false
    private var refresh = true
    private var page = 1
    private var tag: String = ""
    private var platform: String = "pc"
    private val layoutManager = LinearLayoutManager(this)
    private val viewModel: PlayerViewModel by lazy {
        ViewModelProvider(this).get(PlayerViewModel::class.java)
    }
    private val authViewModel: AuthViewModel by lazy {
        ViewModelProvider(this).get(AuthViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_following)
        bnvFeed.selectedItemId = R.id.btFollowing
        bnvFeed.setOnNavigationItemSelectedListener { menuItem ->
            when(menuItem.itemId) {
                R.id.btGlobal -> {
                    val intent = Intent("GLOBAL_FEED")
                        .addCategory("GLOBAL_FEED")
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                    startActivity(intent)
                    overridePendingTransition(0, 0)
                }
                R.id.btLocal -> {
                    val intent = Intent("LOCAL_FEED")
                        .addCategory("LOCAL_FEED")
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                    startActivity(intent)
                    overridePendingTransition(0, 0)
                }
                R.id.btHeroes -> {
                    val intent = Intent("HERO_LIST")
                        .addCategory("HERO_LIST")
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

        viewModel.created.observe(this, Observer { result ->
            loadingContainer.visibility = View.GONE
            popupWindow.dismiss()
            onRefresh()
            Toast.makeText(this, result.msg, Toast.LENGTH_SHORT).show()
        })

        authViewModel.logoutMsg.observe(this, Observer { result ->
            Toast.makeText(this, result.msg, Toast.LENGTH_SHORT).show()
        })

        srlFeedFollowing.setOnRefreshListener { onRefresh() }
        srlFeedFollowing.setColorSchemeColors(ContextCompat.getColor(this, R.color.colorPrimary))
        srlFeedFollowing.setProgressBackgroundColorSchemeColor(ContextCompat.getColor(this, R.color.colorPrimaryDark))
        btSearchPlayer.setOnClickListener { popup() }
        btLogout.setOnClickListener { logout() }

        ivLoading.setBackgroundResource(R.drawable.animation)
        (ivLoading.background as AnimationDrawable).start()

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        configureRecyclerView()
        showPlayers()
    }

    private fun configureRecyclerView() {

        viewModel.playerList.observe(this, Observer { players ->
            followingLoadingContainer.visibility = View.GONE
            srlFeedFollowing.isRefreshing = false
            rvFollowing.adapter = adapter
            isLoading = players.isNullOrEmpty()
            if(refresh) adapter.setPlayers(players)
            else adapter.addPlayers(players)
        })

        rvFollowing.layoutManager = layoutManager
        rvFollowing.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if(dy > 0) {
                    val visibleItemCount = layoutManager.childCount
                    val pastVisibleItem = layoutManager.findFirstCompletelyVisibleItemPosition()
                    val total = adapter.itemCount
                    if(!isLoading && visibleItemCount + pastVisibleItem >= total) {
                        rvFollowing.post { getNextPage() }
                    }
                }
            }
        })
    }

    private fun showPlayers() {
        if(showLoadingIcon) followingLoadingContainer.visibility = View.VISIBLE
        isLoading = true

        try {
            viewModel.followedPlayers()
        } catch (e: Exception) {
            followingLoadingContainer.visibility = View.GONE
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun createPlayer() {
        loadingContainer.visibility = View.VISIBLE
        try {
            viewModel.createPlayer(tag, platform)
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
        refresh = true
        showLoadingIcon = true
        showPlayers()
    }

    private fun onRefresh() {
        showLoadingIcon = false
        page = 1
        refresh = true
        showPlayers()
    }

    private fun getNextPage() {
        page++
        refresh = false
        showLoadingIcon = true
        // showPlayers()
    }

    private fun popup() {
        popupWindow = PopupWindow(this)
        val view = layoutInflater.inflate(R.layout.activity_follow, null)
        popupWindow.contentView = view
        popupWindow.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val btnAdd = view.findViewById<Button>(R.id.btAddBattletag)
        loadingContainer = view.findViewById(R.id.followLoadingContainer)
        val etTag = view.findViewById<EditText>(R.id.etBattletag)
        btnAdd.setOnClickListener {
            tag = etTag.text.toString()
            createPlayer()
        }
        clFollowing.setOnClickListener { popupWindow.dismiss() }
        popupWindow.isFocusable = true
        popupWindow.isTouchable = true
        popupWindow.showAtLocation(clFollowing, Gravity.CENTER, 0, 0)
    }

    private fun logout() {
        authViewModel.logout()
        val uri = Uri.parse("overtracker://login")
        val intent = Intent("LOGIN")
            .addCategory("LOGIN")
        intent.data = uri
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finishAffinity()
    }
}
