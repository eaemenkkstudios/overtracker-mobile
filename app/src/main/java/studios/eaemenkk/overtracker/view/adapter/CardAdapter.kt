package studios.eaemenkk.overtracker.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.feed_list_item.view.*
import studios.eaemenkk.overtracker.R
import studios.eaemenkk.overtracker.domain.Card

class CardAdapter(private val dataSet: Array<Card>): RecyclerView.Adapter<CardAdapter.CardViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.feed_list_item, parent, false)
        return CardViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val card = dataSet[position]
        holder.tag.text = card.player?.tag
        holder.platform.text = card.player?.platform
        holder.type.text = card.type
    }

    class CardViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val tag: TextView = itemView.tvTagNum
        val platform: TextView = itemView.tvPlatform
        val type: TextView = itemView.tvRole
    }
}