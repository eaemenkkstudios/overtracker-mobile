package studios.eaemenkk.overtracker.view.activity

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_following.*
import studios.eaemenkk.overtracker.R
import studios.eaemenkk.overtracker.view.adapter.PlayerAdapter
import studios.eaemenkk.overtracker.viewmodel.PlayerViewModel
import java.lang.Exception

class FollowingActivity : AppCompatActivity() {
    private val mAuth = FirebaseAuth.getInstance()
    private val adapter = PlayerAdapter(this)
    private lateinit var loadingContainer: ConstraintLayout
    private lateinit var popupWindow: PopupWindow
    private var isLoading = false
    private var refresh = true
    private var page = 1
    private var tag: String = ""
    private var platform: String = "pc"
    private val layoutManager = LinearLayoutManager(this)
    private val viewModel: PlayerViewModel by lazy {
        ViewModelProvider(this).get(PlayerViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_following)
        bnvFeed.selectedItemId = R.id.btFollowing
        bnvFeed.setOnNavigationItemSelectedListener { menuItem ->
            when(menuItem.itemId) {
                R.id.btGlobal -> {
                    startActivity(Intent(this, FeedActivity::class.java))
                    overridePendingTransition(0, 0)
                }
                R.id.btLocal -> {
                    startActivity(Intent(this, LocalFeedActivity::class.java))
                    overridePendingTransition(0, 0)
                }
                else -> {
                    layoutManager.smoothScrollToPosition(rvFollowing, object: RecyclerView.State() {}, 0)
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

        srlFeedFollowing.setOnRefreshListener { onRefresh() }
        btnSearchPlayer.setOnClickListener { popup() }

        ivLoading.setBackgroundResource(R.drawable.animation)
        (ivLoading.background as AnimationDrawable).start()

        configureRecyclerView()
        showPlayers()
    }

    private fun configureRecyclerView() {

        viewModel.playerList.observe(this, Observer { players ->
            followingLoadingContainer.visibility = View.GONE
            srlFeedFollowing.isRefreshing = false
            rvFollowing.adapter = adapter
            isLoading = players.isEmpty()
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
                        isLoading = true
                        rvFollowing.post { getNextPage() }
                    }
                }
            }
        })
    }

    private fun showPlayers() {
        followingLoadingContainer.visibility = View.VISIBLE
        mAuth.currentUser?.getIdToken(true)?.addOnCompleteListener {task ->
            if(task.isSuccessful) {
                try {
                    viewModel.followedPlayers(task.result?.token.toString())
                } catch (e: Exception) {
                    followingLoadingContainer.visibility = View.GONE
                    Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
                }
            } else {
                followingLoadingContainer.visibility = View.GONE
                Toast.makeText(this, "Could not load players, please try again...", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun createPlayer() {
        loadingContainer.visibility = View.VISIBLE
        mAuth.currentUser?.getIdToken(true)?.addOnCompleteListener { task ->
            if(task.isSuccessful) {
                try {
                    viewModel.createPlayer(task.result?.token.toString(), tag, platform)
                } catch (e: Exception) {
                    loadingContainer.visibility = View.GONE
                    Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onBackPressed() {
        finish()
        overridePendingTransition(0, 0)
    }

    private fun onRefresh() {
        page = 1
        refresh = true
        showPlayers()
    }

    private fun getNextPage() {
        page++
        refresh = false
        // showPlayers()
    }

    private fun popup() {
        popupWindow = PopupWindow(this)
        val view = layoutInflater.inflate(R.layout.activity_follow, null)
        popupWindow.contentView = view
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


}
