package studios.eaemenkk.overtracker.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
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
        val battleTag = card.player?.tag?.split("#")
        holder.tag.text = battleTag?.get(0)
        holder.tagNum.text = "#${battleTag?.get(1)}"
        holder.platform.text = card.player?.platform?.toUpperCase()
        holder.previous.text = card.sr?.previous;
        holder.current.text = card.sr?.current;
        when (card.role) {
            "support" -> holder.role.setImageResource(R.drawable.support)
            "damage" -> holder.role.setImageResource(R.drawable.damage)
            "tank" -> holder.role.setImageResource(R.drawable.tank)
        }
    }

    class CardViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val tag: TextView = itemView.tvTag
        val tagNum: TextView = itemView.tvTagNum
        val platform: TextView = itemView.tvPlatform
        val role: ImageView = itemView.ivRole
        val previous: TextView = itemView.tvSrPrevious
        val current: TextView = itemView.tvSrCurrent
    }
}