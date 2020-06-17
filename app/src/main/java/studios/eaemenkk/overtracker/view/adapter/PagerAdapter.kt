package studios.eaemenkk.overtracker.view.adapter;

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import studios.eaemenkk.overtracker.domain.Player
import studios.eaemenkk.overtracker.view.fragment.PlayerHistoryFragment
import studios.eaemenkk.overtracker.view.fragment.PlayerStatsFragment

class PagerAdapter(private val player: Player, fm: FragmentManager, private val tabCount: Int): FragmentStatePagerAdapter(fm, tabCount) {
    override fun getItem(position: Int): Fragment {
        return when(position) {
            0 -> PlayerStatsFragment(player)
            1 -> PlayerHistoryFragment(player.scores)
            else -> Fragment()
        }
    }

    override fun getCount(): Int {
        return tabCount
    }

}
