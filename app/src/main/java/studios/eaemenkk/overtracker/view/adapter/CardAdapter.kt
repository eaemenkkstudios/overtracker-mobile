package studios.eaemenkk.overtracker.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.sr_feed_list_item.view.*
import kotlinx.android.synthetic.main.wr_feed_list_item.view.*
import kotlinx.android.synthetic.main.wr_feed_list_item.view.tvPlatform
import kotlinx.android.synthetic.main.wr_feed_list_item.view.tvSrCurrent
import kotlinx.android.synthetic.main.wr_feed_list_item.view.tvSrPrevious
import kotlinx.android.synthetic.main.wr_feed_list_item.view.tvTag
import kotlinx.android.synthetic.main.wr_feed_list_item.view.tvTagNum
import studios.eaemenkk.overtracker.R
import studios.eaemenkk.overtracker.domain.Card
import kotlin.system.measureNanoTime

class CardAdapter(private val dataSet: Array<Card>): RecyclerView.Adapter<CardAdapter.CardViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        when(viewType){
            0 -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.sr_feed_list_item, parent, false)
                return SrCardViewHolder(view)
            }
            1 -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.wr_feed_list_item, parent, false)
                return WrCardViewHolder(view)
            }
            2 -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.endorsement_feed_list_item, parent, false)
                return EndorsementCardViewHolder(view)
            }
            3 -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.main_feed_list_item, parent, false)
                return MainCardViewHolder(view)
            }
            4 -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.highlight_feed_list_item, parent, false)
                return HighlightCardViewHolder(view)
            }
        }

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

    override fun getItemViewType(position: Int): Int {
        when(dataSet[position].type) {
            "sr_update" -> return 0
            "wr_update" -> return 1
            "endorsement_update" -> return 2
            "main_update" -> return 3
            "highlight" -> return 4
        }
    }

    open class CardViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val tag: TextView = itemView.tvTag
        val platform: TextView = itemView.tvPlatform
    }


    class SrCardViewHolder(itemView: View): CardViewHolder(itemView) {
        val tagNum: TextView = itemView.tvTagNum
        val role: ImageView = itemView.ivRole
        val previous: TextView = itemView.tvSrPrevious
        val current: TextView = itemView.tvSrCurrent
    }

    class WrCardViewHolder(itemView: View): CardViewHolder(itemView) {
        val tagNum: TextView = itemView.tvTagNum
        val role: ImageView = itemView.ivRole
        val previous: TextView = itemView.tvSrPrevious
        val current: TextView = itemView.tvSrCurrent
    }

    class MainCardViewHolder(itemView: View): CardViewHolder(itemView) {
        val tagNum: TextView = itemView.tvTagNum
        val role: ImageView = itemView.ivRole
        val previous: TextView = itemView.tvSrPrevious
        val current: TextView = itemView.tvSrCurrent
    }

    class EndorsementCardViewHolder(itemView: View): CardViewHolder(itemView) {
        val tagNum: TextView = itemView.tvTagNum
        val role: ImageView = itemView.ivRole
        val previous: TextView = itemView.tvSrPrevious
        val current: TextView = itemView.tvSrCurrent
    }

    class HighlightCardViewHolder(itemView: View): CardViewHolder(itemView) {
        val tagNum: TextView = itemView.tvTagNum
        val role: ImageView = itemView.ivRole
        val previous: TextView = itemView.tvSrPrevious
        val current: TextView = itemView.tvSrCurrent
    }
}