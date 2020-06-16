package studios.eaemenkk.overtracker.view.adapter;

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class PagerAdapter(fm: FragmentManager, private val tabCount: Int): FragmentStatePagerAdapter(fm, tabCount) {
    override fun getItem(position: Int): Fragment {
        return when(position) {
            0 -> PlayerStatsFragment()
            1 -> PlayerHistoryFragment()
            else -> Fragment()
        }
    }

    override fun getCount(): Int {
        return tabCount
    }

}
