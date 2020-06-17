package studios.eaemenkk.overtracker.view.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_player_stats.view.*
import studios.eaemenkk.overtracker.R
import studios.eaemenkk.overtracker.domain.Player

class PlayerStatsFragment(private val player: Player?) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_player_stats, container, false)
        if(player != null) {
            view.tvInfoMainTime.text = player.now?.main?.time
            view.tvInfoTag.text = player.tag
            view.tvInfoTagNum.text = player.tagNum
            view.ivInfoMainRole.setImageResource(
                when (player.now?.main?.role) {
                    "support" -> R.drawable.support
                    "damage" -> R.drawable.damage
                    "tank" -> R.drawable.tank
                    else -> R.drawable.unknown
                }
            )
            view.tvInfoCurrentDamage.text = player.now?.rank?.damage?.sr.toString()
            view.tvInfoCurrentSupport.text = player.now?.rank?.support?.sr.toString()
            view.tvInfoCurrentTank.text = player.now?.rank?.tank?.sr.toString()
            view.tvInfoMain.text = player.now?.main?.hero
            view.tvInfoPlatform.text = player.platform
            Picasso.get().load(player.now?.portrait).into(view.ivInfoMain)
            Picasso.get().load(player.portrait).into(view.ivInfoPortrait)
            val clickListener = View.OnClickListener {
                val intent = Intent("OVERTRACKER_HERO_INFO")
                    .addCategory("OVERTRACKER_HERO_INFO")
                intent.putExtra("heroName", player.now?.main?.friendlyHero)
                startActivity(intent)
            }
            view.ivInfoMain.setOnClickListener(clickListener)
            view.tvInfoMain.setOnClickListener(clickListener)
            view.ivInfoEndorsement.setImageResource(
                when (player.now?.endorsement) {
                    "1" -> R.drawable.endorsement_1
                    "2" -> R.drawable.endorsement_2
                    "3" -> R.drawable.endorsement_3
                    "4" -> R.drawable.endorsement_4
                    "5" -> R.drawable.endorsement_5
                    else -> R.drawable.unknown
                }
            )
        }
        return view
    }
}