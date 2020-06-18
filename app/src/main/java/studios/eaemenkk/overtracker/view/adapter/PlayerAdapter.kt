package studios.eaemenkk.overtracker.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import studios.eaemenkk.overtracker.domain.Player
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.player_list_item.view.*
import studios.eaemenkk.overtracker.R
import studios.eaemenkk.overtracker.view.fragment.DialogFragmentWindow

class PlayerAdapter(private val supportFragmentManager: FragmentManager) : RecyclerView.Adapter<PlayerAdapter.PlayerViewHolder>() {
    private var dataSet = ArrayList<Player>()

    override fun onCreateViewHolder( parent: ViewGroup, viewType: Int): PlayerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.player_list_item, parent, false)
        return PlayerViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    override fun onBindViewHolder(holder: PlayerViewHolder, position: Int) {
        val player = dataSet[position]

        holder.itemView.setOnClickListener {
            val dialogFragment = DialogFragmentWindow(player.id.toString())
            dialogFragment.show(supportFragmentManager, "")
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

    private fun removePlayer(index: Int) {
        dataSet.removeAt(index)
        notifyDataSetChanged()
    }

    private fun addPlayer(player: Player?) {
        if(player != null) {
            dataSet.add(player)
            notifyDataSetChanged()
        }
    }

    fun addPlayers(players: ArrayList<Player>?) {
        if(players != null) {
            dataSet.addAll(players)
            notifyDataSetChanged()
        }
    }

    fun setPlayers(players: ArrayList<Player>?) {
        if(players != null) {
            dataSet = players
            notifyDataSetChanged()
        }
    }

    class PlayerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tag: TextView = itemView.tvPlayerTag
        val tagNum: TextView = itemView.tvPlayerTagNum
        val platform: TextView = itemView.tvPlayerPlatform
        val endorsement: ImageView = itemView.ivPlayerEndorsement
        val portrait: ImageView = itemView.ivPlayerPortrait
        val role: ImageView = itemView.ivPlayerRole
    }
}
