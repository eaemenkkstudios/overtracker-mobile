package studios.eaemenkk.overtracker.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.sr_feed_list_item.view.*
import kotlinx.android.synthetic.main.wr_feed_list_item.view.*
import kotlinx.android.synthetic.main.endorsement_feed_list_item.view.*
import kotlinx.android.synthetic.main.highlight_feed_list_item.view.*
import kotlinx.android.synthetic.main.main_feed_list_item.view.*
import studios.eaemenkk.overtracker.R
import studios.eaemenkk.overtracker.domain.Card

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
            else -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.highlight_feed_list_item, parent, false)
                return HighlightCardViewHolder(view)
            }
        }
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val card = dataSet[position]
        when (holder) {
            is SrCardViewHolder -> {
                val battleTag = card.player?.tag?.split("#")
                holder.tag.text = battleTag?.get(0)
                holder.tagNum.text = "#${battleTag?.get(1)}"
                holder.platform.text = card.player?.platform?.toUpperCase()
                holder.previous.text = card.sr?.previous;
                holder.current.text = card.sr?.current;
                holder.role.setImageResource(when (card.role) {
                    "support" -> R.drawable.support
                    "damage" -> R.drawable.damage
                    else -> R.drawable.tank
                })
            }
            is WrCardViewHolder -> {
                val battleTag = card.player?.tag?.split("#")
                holder.tag.text = battleTag?.get(0)
                holder.tagNum.text = "#${battleTag?.get(1)}"
                holder.platform.text = card.player?.platform?.toUpperCase()
                holder.previous.text = card.winrate?.previous;
                holder.current.text = card.winrate?.current;
            }
            is MainCardViewHolder -> {
                holder.platform.text = card.player?.platform?.toUpperCase()
            }
            is EndorsementCardViewHolder -> {
                holder.platform.text = card.player?.platform?.toUpperCase()
            }
            is HighlightCardViewHolder -> {
                holder.platform.text = card.player?.platform?.toUpperCase()
            }
        }

    }

    override fun getItemViewType(position: Int): Int {
        return when(dataSet[position].type) {
            "sr_update" -> 0
            "winrate_update" -> 1
            "endorsement_update" -> 2
            "main_update" -> 3
            "highlight" -> 4
            else -> 5
        }
    }

    abstract class CardViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    class SrCardViewHolder(itemView: View): CardViewHolder(itemView) {
        val tag: TextView = itemView.tvSrTag
        val tagNum: TextView = itemView.tvSrTagNum
        val platform: TextView = itemView.tvSrPlatform
        val role: ImageView = itemView.ivSrRole
        val previous: TextView = itemView.tvSrPrevious
        val current: TextView = itemView.tvSrCurrent
    }

    class WrCardViewHolder(itemView: View): CardViewHolder(itemView) {
        val tag: TextView = itemView.tvWrTag
        val tagNum: TextView = itemView.tvWrTagNum
        val platform: TextView = itemView.tvWrPlatform
        val previous: TextView = itemView.tvWrPrevious
        val current: TextView = itemView.tvWrCurrent
    }

    class MainCardViewHolder(itemView: View): CardViewHolder(itemView) {
        val tag: TextView = itemView.tvMainTag
        val tagNum: TextView = itemView.tvMainTagNum
        val platform: TextView = itemView.tvMainPlatform
        val role: ImageView = itemView.ivMainRole
        val previous: TextView = itemView.tvMainPrevious
        val current: TextView = itemView.tvMainCurrent
    }

    class EndorsementCardViewHolder(itemView: View): CardViewHolder(itemView) {
        val tag: TextView = itemView.tvEndorsementTag
        val tagNum: TextView = itemView.tvEndorsementTagNum
        val platform: TextView = itemView.tvEndorsementPlatform
        val role: ImageView = itemView.ivEndorsementRole
        val previous: TextView = itemView.tvEndorsementPrevious
        val current: TextView = itemView.tvEndorsementCurrent
    }

    class HighlightCardViewHolder(itemView: View): CardViewHolder(itemView) {
        val tag: TextView = itemView.tvHighlightTag
        val tagNum: TextView = itemView.tvHighlightTagNum
        val platform: TextView = itemView.tvHighlightPlatform
        val role: ImageView = itemView.ivHighlightRole
        val previous: TextView = itemView.tvHighlightPrevious
        val current: TextView = itemView.tvHighlightCurrent
    }
}