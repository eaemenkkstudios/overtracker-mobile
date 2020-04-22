package studios.eaemenkk.overtracker.view.activity

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_following.*
import studios.eaemenkk.overtracker.R
import studios.eaemenkk.overtracker.view.adapter.PlayerAdapter
import studios.eaemenkk.overtracker.viewmodel.PlayerViewModel
import java.lang.Exception

class FollowingActivity : AppCompatActivity() {
    private val mAuth = FirebaseAuth.getInstance()
    private var loadingAnimation = AnimationDrawable()
    private val adapter = PlayerAdapter(this)
    private var isLoading = false
    private var refresh = true
    private var page = 1
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
        srlFeedFollowing.setOnRefreshListener { onRefresh() }

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

    override fun onBackPressed() {
        finish()
        overridePendingTransition(0, 0)
    }

    private fun onRefresh() {
        page = 1
        refresh = true
        showPlayers()
    }

    fun getNextPage() {
        page++
        refresh = false
        // showPlayers()
    }


}
