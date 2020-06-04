package studios.eaemenkk.overtracker.view.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.*
import com.google.android.gms.ads.formats.MediaView
import com.google.android.gms.ads.formats.NativeAdOptions
import com.google.android.gms.ads.formats.UnifiedNativeAd
import com.google.android.gms.ads.formats.UnifiedNativeAdView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.native_ad_list_item.view.*
import kotlinx.android.synthetic.main.endorsement_feed_list_item.view.*
import kotlinx.android.synthetic.main.highlight_feed_list_item.view.*
import kotlinx.android.synthetic.main.main_feed_list_item.view.*
import kotlinx.android.synthetic.main.sr_feed_list_item.view.*
import kotlinx.android.synthetic.main.wr_feed_list_item.view.*
import studios.eaemenkk.overtracker.R
import studios.eaemenkk.overtracker.domain.AdCard
import studios.eaemenkk.overtracker.domain.Card
import studios.eaemenkk.overtracker.view.activity.InfoActivity

private const val AD_INTERVAL = 8

class CardAdapter(private val context: Context): RecyclerView.Adapter<CardAdapter.CardViewHolder>() {
    private var dataSet = ArrayList<Card>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        return when(viewType){
            0 -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.sr_feed_list_item, parent, false)
                SrCardViewHolder(view)
            }
            1 -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.wr_feed_list_item, parent, false)
                WrCardViewHolder(view)
            }
            2 -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.endorsement_feed_list_item, parent, false)
                EndorsementCardViewHolder(view)
            }
            3 -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.main_feed_list_item, parent, false)
                MainCardViewHolder(view)
            }
            4 -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.highlight_feed_list_item, parent, false)
                HighlightCardViewHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.native_ad_list_item, parent, false)
                AdCardViewHolder(view)
            }
        }
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val card = dataSet[position]
        if(holder !is AdCardViewHolder) {
            holder.itemView.setOnClickListener {
                val intent = Intent("OVERTRACKER_PLAYER_INFO")
                    .addCategory("OVERTRACKER_PLAYER_INFO")
                intent.putExtra("playerId", card.player?.id)
                context.startActivity(intent)
            }
        }

        when (holder) {
            is SrCardViewHolder -> {
                holder.tag.text = card.player?.tag
                holder.tagNum.text = card.player?.tagNum
                holder.platform.text = card.player?.platform
                holder.previous.text = card.sr?.previous
                holder.current.text = card.sr?.current
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
                holder.previous.text = card.winrate?.previous
                holder.current.text = card.winrate?.current
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
                })
                holder.current.setImageResource(
                    when(card.endorsement?.current){
                        "1" -> R.drawable.endorsement_1
                        "2" -> R.drawable.endorsement_2
                        "3" -> R.drawable.endorsement_3
                        "4" -> R.drawable.endorsement_4
                        "5" -> R.drawable.endorsement_5
                        else -> R.drawable.unknown
                    })
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
            is AdCardViewHolder -> {
                val adLoader = AdLoader.Builder(context, context.getString(R.string.ad_native_id))
                    .forUnifiedNativeAd { unifiedNativeAd ->
                        holder.adView.headlineView = holder.headlineView
                        holder.adView.bodyView = holder.bodyView
                        holder.adView.callToActionView = holder.callToActionView
                        holder.adView.iconView = holder.iconView
                        holder.adView.priceView = holder.priceView
                        holder.adView.starRatingView = holder.starRatingView
                        holder.adView.storeView = holder.storeView
                        holder.adView.advertiserView = holder.advertiserView
                        holder.adView.mediaView = holder.mediaView
                        populateNativeAdView(unifiedNativeAd, holder.adView)
                    }
                    .withAdListener(object: AdListener() {
                        override fun onAdFailedToLoad(errorCode: Int) {
                            removeCard(position)
                        }
                    })
                    .withNativeAdOptions(NativeAdOptions.Builder().build())
                    .build()
                adLoader.loadAd(AdRequest.Builder().build())
            }
        }

    }

    override fun getItemViewType(position: Int): Int {
        return when(dataSet[position].cardType) {
            "sr_update" -> 0
            "winrate_update" -> 1
            "endorsement_update" -> 2
            "main_update" -> 3
            "highlight" -> 4
            else -> 5
        }
    }

    private fun removeCard(index: Int) {
        dataSet.removeAt(index)
        notifyDataSetChanged()
    }

    private fun addCard(index: Int, card: Card?) {
        if(card != null) {
            dataSet.add(card)
            if(index % AD_INTERVAL == AD_INTERVAL - 1) {
                dataSet.add(AdCard())
            }
            notifyDataSetChanged()
        }
    }

    fun addCards(cards: ArrayList<Card>?) {
        cards?.forEachIndexed { index, card -> addCard(index, card) }
    }

    fun setCards(cards: ArrayList<Card>?) {
        if(cards != null) {
            dataSet = ArrayList()
            cards.forEachIndexed { index, card -> addCard(index, card)
            }
        }
    }

    private fun populateNativeAdView(nativeAd: UnifiedNativeAd, adView: UnifiedNativeAdView) {
        (adView.headlineView as TextView).text = nativeAd.headline
        (adView.bodyView as TextView).text = nativeAd.body
        (adView.callToActionView as Button).text = "${nativeAd.callToAction} "

        val icon = nativeAd.icon
        if (icon == null) {
            adView.iconView.visibility = View.INVISIBLE
        } else {
            (adView.iconView as ImageView).setImageDrawable(icon.drawable)
            adView.iconView.visibility = View.VISIBLE
        }
        if (nativeAd.price == null) {
            adView.priceView.visibility = View.INVISIBLE
        } else {
            adView.priceView.visibility = View.VISIBLE
            (adView.priceView as TextView).text = nativeAd.price
        }
        if (nativeAd.store == null) {
            adView.storeView.visibility = View.INVISIBLE
        } else {
            adView.storeView.visibility = View.VISIBLE
            (adView.storeView as TextView).text = nativeAd.store
        }
        if (nativeAd.starRating == null) {
            adView.starRatingView.visibility = View.INVISIBLE
        } else {
            (adView.starRatingView as RatingBar).rating = nativeAd.starRating.toFloat()
            adView.starRatingView.visibility = View.VISIBLE
        }
        if (nativeAd.advertiser == null) {
            adView.advertiserView.visibility = View.INVISIBLE
        } else {
            (adView.advertiserView as TextView).text = nativeAd.advertiser
            adView.advertiserView.visibility = View.VISIBLE
        }
        if (nativeAd.mediaContent == null) {
            adView.mediaView.visibility = View.GONE
        } else {
            (adView.mediaView as MediaView).setMediaContent(nativeAd.mediaContent)
            adView.mediaView.visibility = View.VISIBLE
        }

        adView.setNativeAd(nativeAd)
    }

    abstract class CardViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    class AdCardViewHolder(itemView: View): CardViewHolder(itemView) {
        val adView: UnifiedNativeAdView = itemView.ad_view
        val headlineView: TextView = itemView.ad_headline
        val bodyView: TextView = itemView.ad_body
        val callToActionView: Button = itemView.ad_call_to_action
        val iconView: ImageView = itemView.ad_icon
        val priceView: TextView = itemView.ad_price
        val starRatingView: RatingBar = itemView.ad_stars
        val storeView: TextView = itemView.ad_store
        val advertiserView: TextView = itemView.ad_advertiser
        val mediaView: MediaView = itemView.ad_media
    }

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