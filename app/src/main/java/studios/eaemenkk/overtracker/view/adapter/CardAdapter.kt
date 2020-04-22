package studios.eaemenkk.overtracker.view.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.sr_feed_list_item.view.*
import kotlinx.android.synthetic.main.wr_feed_list_item.view.*
import kotlinx.android.synthetic.main.endorsement_feed_list_item.view.*
import kotlinx.android.synthetic.main.highlight_feed_list_item.view.*
import kotlinx.android.synthetic.main.main_feed_list_item.view.*
import studios.eaemenkk.overtracker.R
import studios.eaemenkk.overtracker.domain.Card
import studios.eaemenkk.overtracker.view.activity.InfoActivity

class CardAdapter(private val context: Context): RecyclerView.Adapter<CardAdapter.CardViewHolder>() {
    private var dataSet = ArrayList<Card>()

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
        holder.itemView.setOnClickListener {
            val intent = Intent(context, InfoActivity::class.java)
            intent.putExtra("playerId", card.player?.id)
            context.startActivity(intent)
        }
        when (holder) {
            is SrCardViewHolder -> {
                holder.tag.text = card.player?.tag
                holder.tagNum.text = card.player?.tagNum
                holder.platform.text = card.player?.platform
                holder.previous.text = card.sr?.previous;
                holder.current.text = card.sr?.current;
                holder.role.setImageResource(when (card.role) {
                    "support" -> R.drawable.support
                    "damage" -> R.drawable.damage
                    "tank" -> R.drawable.tank
                    else -> R.drawable.unknown
                })
            }
            is WrCardViewHolder -> {
                holder.tag.text = card.player?.tag
                holder.tagNum.text = card.player?.tagNum
                holder.platform.text = card.player?.platform
                holder.previous.text = card.winrate?.previous;
                holder.current.text = card.winrate?.current;
            }
            is MainCardViewHolder -> {
                holder.tag.text = card.player?.tag
                holder.tagNum.text = card.player?.tagNum
                holder.platform.text = card.player?.platform
                holder.previous.text = card.previous?.hero
                holder.current.text = card.current?.hero
                Picasso.get().load(card.previous?.portrait).into(holder.previousImg)
                Picasso.get().load(card.current?.portrait).into(holder.currentImg)
            }
            is EndorsementCardViewHolder -> {
                holder.tag.text = card.player?.tag
                holder.tagNum.text = card.player?.tagNum
                holder.platform.text = card.player?.platform
                holder.previous.setImageResource(
                    when(card.endorsement?.previous){
                    "1" -> R.drawable.endorsement_1
                    "2" -> R.drawable.endorsement_2
                    "3" -> R.drawable.endorsement_3
                    "4" -> R.drawable.endorsement_4
                    "5" -> R.drawable.endorsement_5
                        else -> R.drawable.unknown
                });
                holder.current.setImageResource(
                    when(card.endorsement?.current){
                        "1" -> R.drawable.endorsement_1
                        "2" -> R.drawable.endorsement_2
                        "3" -> R.drawable.endorsement_3
                        "4" -> R.drawable.endorsement_4
                        "5" -> R.drawable.endorsement_5
                        else -> R.drawable.unknown
                    });
            }
            is HighlightCardViewHolder -> {
                holder.tag.text = card.player?.tag
                holder.tagNum.text = card.player?.tagNum
                holder.role.setImageResource(when (card.main?.role) {
                    "support" -> R.drawable.support
                    "damage" -> R.drawable.damage
                    "tank" -> R.drawable.tank
                    else -> R.drawable.unknown
                })
                holder.sr.text = card.sr?.current
                holder.srSlope.setImageResource(when (card.sr?.slope) {
                    "increasing" -> R.drawable.arrow_up
                    "tied" -> R.drawable.arrow_middle
                    "decreasing" -> R.drawable.arrow_down
                    else -> R.drawable.unknown
                })
                holder.wr.text = card.winrate?.current
                holder.wrSlope.setImageResource(when (card.winrate?.slope) {
                    "increasing" -> R.drawable.arrow_up
                    "tied" -> R.drawable.arrow_middle
                    "decreasing" -> R.drawable.arrow_down
                    else -> R.drawable.unknown
                })
                holder.time.text = card.main?.time
                Picasso.get().load(card.main?.portrait).into(holder.mainImg)
                holder.main.text = card.main?.current
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

    fun addCard(card: Card) {
        dataSet.add(card)
        notifyDataSetChanged()
    }

    fun addCards(cards: ArrayList<Card>) {
        dataSet.addAll(dataSet.size, cards)
        notifyDataSetChanged()

    }

    fun setCards(cards: ArrayList<Card>) {
        dataSet = cards
        notifyDataSetChanged()
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
        val previous: TextView = itemView.tvMainPrevious
        val current: TextView = itemView.tvMainCurrent
        val previousImg: ImageView = itemView.ivMainPrevious
        val currentImg: ImageView = itemView.ivMainCurrent
    }

    class EndorsementCardViewHolder(itemView: View): CardViewHolder(itemView) {
        val tag: TextView = itemView.tvEndorsementTag
        val tagNum: TextView = itemView.tvEndorsementTagNum
        val platform: TextView = itemView.tvEndorsementPlatform
        val previous: ImageView = itemView.ivEndorsementPrevious
        val current: ImageView = itemView.ivEndorsementCurrent
    }

    class HighlightCardViewHolder(itemView: View): CardViewHolder(itemView) {
        val tag: TextView = itemView.tvHighlightTag
        val tagNum: TextView = itemView.tvHighlightTagNum
        val role: ImageView = itemView.ivHighlightRole
        val time: TextView = itemView.tvHighlightTime
        val main: TextView = itemView.tvHighlightMain
        val mainImg: ImageView = itemView.ivHighlightMain
        val sr: TextView = itemView.tvHighlightSr
        val srSlope: ImageView = itemView.ivHighlightSrSlope
        val wr: TextView = itemView.tvHighlightWr
        val wrSlope: ImageView = itemView.ivHighlightWrSlope
    }
}