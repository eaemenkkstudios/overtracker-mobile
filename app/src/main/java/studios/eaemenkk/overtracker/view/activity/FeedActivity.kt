package studios.eaemenkk.overtracker.view.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_feed.*
import kotlinx.android.synthetic.main.activity_profile.*
import studios.eaemenkk.overtracker.R
import studios.eaemenkk.overtracker.view.adapter.CardAdapter
import studios.eaemenkk.overtracker.viewmodel.CardViewModel

class FeedActivity: AppCompatActivity() {

    private val viewModel: CardViewModel by lazy {
        ViewModelProvider(this).get(CardViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed)
        btProfile.setOnClickListener { profile() }
        configureRecyclerView()
        getFeed()
    }

    fun getFeed() {
        viewModel.cardList.observe(this, Observer { cards ->
            feedLoadingContainer.visibility = View.GONE
            val adapter = CardAdapter(cards)
            rvFeed.adapter = adapter
        })
        feedLoadingContainer.visibility = View.VISIBLE
        viewModel.getFeed()
    }

    fun configureRecyclerView() {
        rvFeed.layoutManager = LinearLayoutManager(this)
    }

    fun profile() {
        startActivity(Intent(this, ProfileActivity::class.java))
    }
}