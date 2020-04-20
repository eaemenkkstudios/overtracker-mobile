package studios.eaemenkk.overtracker.view.adapter

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
import studios.eaemenkk.overtracker.view.activity.FeedActivity

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
        val battleTag = card.player?.tag?.split("#")
        when (holder) {
            is SrCardViewHolder -> {
                holder.tag.text = battleTag?.get(0)
                holder.tagNum.text = "#${battleTag?.get(1)}"
                holder.platform.text = card.player?.platform?.toUpperCase()
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
                holder.tag.text = battleTag?.get(0)
                holder.tagNum.text = "#${battleTag?.get(1)}"
                holder.platform.text = card.player?.platform?.toUpperCase()
                holder.previous.text = card.winrate?.previous;
                holder.current.text = card.winrate?.current;
            }
            is MainCardViewHolder -> {
                holder.tag.text = battleTag?.get(0)
                holder.tagNum.text = "#${battleTag?.get(1)}"
                holder.platform.text = card.player?.platform?.toUpperCase()
                holder.previous.text = card.previous?.hero
                holder.current.text = card.current?.hero
                Picasso.get().load("https://d1u1mce87gyfbn.cloudfront.net/hero/${card.previous?.hero}/hero-select-portrait.png").into(holder.previousImg)
                Picasso.get().load("https://d1u1mce87gyfbn.cloudfront.net/hero/${card.current?.hero}/hero-select-portrait.png").into(holder.currentImg)
            }
            is EndorsementCardViewHolder -> {
                holder.platform.text = card.player?.platform?.toUpperCase()
                holder.tag.text = battleTag?.get(0)
                holder.tagNum.text = "#${battleTag?.get(1)}"
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
                holder.tag.text = battleTag?.get(0)
                holder.tagNum.text = "#${battleTag?.get(1)}"
                holder.role.setImageResource(when (card.main?.role) {
                    "support" -> R.drawable.support
                    "damage" -> R.drawable.damage
                    "tank" -> R.drawable.tank
                    else -> R.drawable.unknown
                })
                holder.sr.text = "${card.sr?.current} sr"
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
                holder.time.text = "${card.main?.time?.split(":")?.get(0)}h"
                var heroName = card.main?.current
                heroName = when(heroName) {
                    "dva" -> "d.va"
                    "lucio" -> "lúcio"
                    "torbjorn" -> "torbjörn"
                    "wreckingball" -> "wrecking ball"
                    "soldier76" -> "soldier: 76"
                    else -> heroName
                }
                Picasso.get().load("https://overtracker-api.herokuapp.com/images/${card.main?.current}.png").into(holder.mainImg)
                holder.main.text = "main ${heroName} "
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

    interface OnCardListener {
        fun onCardClick(position: Int)
    }
}