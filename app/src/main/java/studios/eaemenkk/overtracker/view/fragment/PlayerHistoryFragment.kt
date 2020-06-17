package studios.eaemenkk.overtracker.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_player_history.view.rvInfoScores
import studios.eaemenkk.overtracker.R
import studios.eaemenkk.overtracker.domain.Score
import studios.eaemenkk.overtracker.view.adapter.PlayerScoreAdapter

class PlayerHistoryFragment(private val scores: Array<Score>?) : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_player_history, container, false)
        if(scores != null) {
            val adapter = PlayerScoreAdapter(scores)
            view.rvInfoScores.layoutManager = LinearLayoutManager(context)
            view.rvInfoScores.adapter = adapter
        }
        return view
    }
}