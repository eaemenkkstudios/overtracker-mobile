package studios.eaemenkk.overtracker.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_following.*
import studios.eaemenkk.overtracker.R
import studios.eaemenkk.overtracker.view.adapter.PlayerAdapter
import studios.eaemenkk.overtracker.viewmodel.PlayerViewModel

class ProfileActivity : AppCompatActivity() {

    private val mAuth = FirebaseAuth.getInstance()

    private val viewModel: PlayerViewModel by lazy {
        ViewModelProvider(this).get(PlayerViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_following)
        configureRecyclerView()
        showPlayers()
    }

    private fun configureRecyclerView() {
        rvFollowing.layoutManager = LinearLayoutManager(this)
    }

    private fun showPlayers() {
        viewModel.playerList.observe(this, Observer { players ->
            val adapter = PlayerAdapter(players)
            rvFollowing.adapter = adapter
        })
        val operation = mAuth.currentUser?.getIdToken(true)
        operation?.addOnCompleteListener {task ->
            if(task.isSuccessful) {
                viewModel.followedPlayers(task.result?.token.toString())
            }
        }
    }
}
