package studios.eaemenkk.overtracker.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import studios.eaemenkk.overtracker.R
import studios.eaemenkk.overtracker.view.adapter.PlayerAdapter
import studios.eaemenkk.overtracker.viewmodel.PlayerViewModel

class MainActivity : AppCompatActivity() {

    private val mAuth = FirebaseAuth.getInstance()

    private val viewModel: PlayerViewModel by lazy {
        ViewModelProvider(this).get(PlayerViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        configureRecyclerView()
        showPlayers()
    }

    fun configureRecyclerView() {
        rvPlayers.layoutManager = LinearLayoutManager(this)
    }

    fun showPlayers() {
        viewModel.playerList.observe(this, Observer { players ->
            val adapter = PlayerAdapter(players)
            rvPlayers.adapter = adapter
        })
        val operation = mAuth.currentUser?.getIdToken(true)
        operation?.addOnCompleteListener {task ->
            if(task.isSuccessful) {
                viewModel.followedPlayers(task.result?.token.toString())
            }
        }
    }
}
