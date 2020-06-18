package studios.eaemenkk.overtracker.view.fragment

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.profile_popup.view.*
import studios.eaemenkk.overtracker.R
import studios.eaemenkk.overtracker.view.adapter.PagerAdapter
import studios.eaemenkk.overtracker.viewmodel.PlayerViewModel


class DialogFragmentWindow(private val data: String): DialogFragment() {
    private lateinit var pagerAdapter: PagerAdapter
    private val viewModel: PlayerViewModel by lazy {
        ViewModelProvider(this).get(PlayerViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = layoutInflater.inflate(R.layout.profile_popup, container)
        view.loadingContainer.visibility = View.VISIBLE
        viewModel.playerDetails.observe(this, Observer { player ->
            view.loadingContainer.visibility = View.GONE
            player.id = data
            pagerAdapter = PagerAdapter(player, childFragmentManager,2)
            val viewPager = view.findViewById<ViewPager>(R.id.vpProfile)
            viewPager.adapter = pagerAdapter
            val tabLayout = view.findViewById<TabLayout>(R.id.tabDots)
            tabLayout.setupWithViewPager(viewPager, true)
        })

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        viewModel.playerInfo(data)
        return view
    }
    override fun onResume() {
        super.onResume()
        val window = dialog!!.window
        window!!.setLayout(1000, 1150)
        //window.setGravity(Gravity.CENTER)
        //TODO:
    }
}