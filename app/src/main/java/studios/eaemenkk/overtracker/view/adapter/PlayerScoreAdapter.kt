package studios.eaemenkk.overtracker.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.score_list_item.view.*
import studios.eaemenkk.overtracker.R
import studios.eaemenkk.overtracker.domain.Score

class PlayerScoreAdapter(private val dataSet: Array<Score>): RecyclerView.Adapter<PlayerScoreAdapter.PlayerInfoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerInfoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.score_list_item, parent, false)
        return PlayerInfoViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    override fun onBindViewHolder(holder: PlayerInfoViewHolder, position: Int) {
        val score = dataSet[position]
        holder.damage.text = score.rank?.damage?.sr.toString()
        holder.support.text = score.rank?.support?.sr.toString()
        holder.tank.text = score.rank?.tank?.sr.toString()
        holder.date.text = score.date
    }

    class PlayerInfoViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val damage: TextView = itemView.tvScoreDamage
        val support: TextView = itemView.tvScoreSupport
        val tank: TextView = itemView.tvScoreTank
        val date: TextView = itemView.tvScoreDate
    }
}