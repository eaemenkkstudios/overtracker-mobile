package studios.eaemenkk.overtracker.view.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import studios.eaemenkk.overtracker.domain.Player
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.banner_ad_list_item.view.*
import kotlinx.android.synthetic.main.player_list_item.view.*
import studios.eaemenkk.overtracker.R
import studios.eaemenkk.overtracker.domain.AdPlayer
import studios.eaemenkk.overtracker.view.activity.InfoActivity

private const val AD_INTERVAL = 5

class PlayerAdapter(private val context: Context) : RecyclerView.Adapter<PlayerAdapter.PlayerViewHolder>() {
    private var dataSet = ArrayList<Player>()

    override fun onCreateViewHolder( parent: ViewGroup, viewType: Int): PlayerViewHolder {
        return when(viewType) {
            0 -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.player_list_item, parent, false)
                PlayerItemViewHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.banner_ad_list_item, parent, false)
                AdPlayerViewHolder(view)
            }
        }

    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    override fun getItemViewType(position: Int): Int {
        return if(dataSet[position] is AdPlayer) 1 else 0
    }

    override fun onBindViewHolder(holder: PlayerViewHolder, position: Int) {
        val player = dataSet[position]

        when (holder) {
            is AdPlayerViewHolder -> {
                val adView = AdView(context)
                adView.adSize = AdSize.FULL_BANNER
                adView.adUnitId = context.getString(R.string.ad_banner_id)
                adView.adListener = object: AdListener() {
                    override fun onAdFailedToLoad(errorCode: Int) {
                        removePlayer(position)
                    }
                }
                adView.loadAd(AdRequest.Builder().build())
                holder.itemView.cardView.addView(adView)
            }
            is PlayerItemViewHolder -> {
                holder.itemView.setOnClickListener {
                    val intent = Intent("OVERTRACKER_PLAYER_INFO").addCategory("OVERTRACKER_PLAYER_INFO")
                    intent.putExtra("playerId", player.id)
                    context.startActivity(intent)
                }
                holder.tag.text = player.tag
                holder.tagNum.text = player.tagNum
                holder.platform.text = player.platform
                Picasso.get().load(player.portrait).into(holder.portrait)
                holder.role.setImageResource(when (player.current?.role) {
                    "support" -> R.drawable.support
                    "damage" -> R.drawable.damage
                    "tank" -> R.drawable.tank
                    else -> R.drawable.unknown
                })
                holder.endorsement.setImageResource(
                    when(player.current?.endorsement) {
                        "1" -> R.drawable.endorsement_1
                        "2" -> R.drawable.endorsement_2
                        "3" -> R.drawable.endorsement_3
                        "4" -> R.drawable.endorsement_4
                        "5" -> R.drawable.endorsement_5
                        else -> R.drawable.unknown
                    })
            }
        }
    }

    private fun removePlayer(index: Int) {
        dataSet.removeAt(index)
        notifyDataSetChanged()
    }

    private fun addPlayer(index: Int, player: Player?) {
        if(player != null) {
            dataSet.add(player)
            if(index % AD_INTERVAL == AD_INTERVAL - 1) {
                dataSet.add(AdPlayer())
            }
            notifyDataSetChanged()
        }
    }

    fun addPlayers(players: ArrayList<Player>?) {
        if(players != null) {
            dataSet.addAll(players)
            players.forEachIndexed { index, player ->  addPlayer(index, player)}
        }
    }

    fun setPlayers(players: ArrayList<Player>?) {
        if(players != null) {
            dataSet = ArrayList()
            players.forEachIndexed { index, player ->  addPlayer(index, player)}
        }
    }

    abstract class PlayerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    class PlayerItemViewHolder(itemView: View) : PlayerViewHolder(itemView) {
        val tag: TextView = itemView.tvPlayerTag
        val tagNum: TextView = itemView.tvPlayerTagNum
        val platform: TextView = itemView.tvPlayerPlatform
        val endorsement: ImageView = itemView.ivPlayerEndorsement
        val portrait: ImageView = itemView.ivPlayerPortrait
        val role: ImageView = itemView.ivPlayerRole
    }

    class AdPlayerViewHolder(itemView: View) : PlayerViewHolder(itemView)
}
