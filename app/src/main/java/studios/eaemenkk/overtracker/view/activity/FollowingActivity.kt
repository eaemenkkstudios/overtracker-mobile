package studios.eaemenkk.overtracker.view.activity

import android.content.Intent
import android.graphics.Color
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
    private val adapter = PlayerAdapter(this, supportFragmentManager)
    private var showLoadingIcon = true
    private var isLoading = false
    private var refresh = true
    private var page = 1
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

        authViewModel.logoutMsg.observe(this, Observer { result ->
            Toast.makeText(this, result.msg, Toast.LENGTH_SHORT).show()
        })

        srlFeedFollowing.setOnRefreshListener { onRefresh() }
        srlFeedFollowing.setColorSchemeColors(ContextCompat.getColor(this, R.color.colorPrimary))
        srlFeedFollowing.setProgressBackgroundColorSchemeColor(ContextCompat.getColor(this, R.color.colorPrimaryDark))
        btLogout.setOnClickListener { logout() }
        ivBack.setOnClickListener { finish() }
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

    private fun logout() {
        authViewModel.logout()
        val uri = Uri.parse("overtracker://login")
        val intent = Intent("OVERTRACKER_LOGIN")
            .addCategory("OVERTRACKER_LOGIN")
            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.data = uri

        startActivity(intent)
        finishAffinity()
    }
}
