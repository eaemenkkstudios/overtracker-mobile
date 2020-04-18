package studios.eaemenkk.overtracker.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import studios.eaemenkk.overtracker.domain.Player
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.player_list_item.view.*
import studios.eaemenkk.overtracker.R

class PlayerAdapter(private val dataSet: Array<Player>) : RecyclerView.Adapter<PlayerAdapter.PlayerViewHolder>() {

    override fun onCreateViewHolder( parent: ViewGroup, viewType: Int): PlayerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.player_list_item, parent, false)
        return PlayerViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    override fun onBindViewHolder(holder: PlayerViewHolder, position: Int) {
        val player = dataSet[position]
        holder.tag.text = player.tag
        holder.platform.text = player.platform

    }

    class PlayerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tag: TextView = itemView.tvTag
        val platform: TextView = itemView.tvPlatform
    }
}
