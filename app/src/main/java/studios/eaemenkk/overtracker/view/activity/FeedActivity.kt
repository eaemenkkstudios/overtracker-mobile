package studios.eaemenkk.overtracker.view.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.AnimationDrawable
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.maps.LocationSource
import kotlinx.android.synthetic.main.activity_feed.*
import studios.eaemenkk.overtracker.R
import studios.eaemenkk.overtracker.view.adapter.CardAdapter
import studios.eaemenkk.overtracker.viewmodel.CardViewModel
import studios.eaemenkk.overtracker.viewmodel.PlayerViewModel

class FeedActivity: AppCompatActivity() {
    private var page = 1
    private var refresh = true
    private var isLoading = false
    private var showLoadingIcon = true
    private val layoutManager = LinearLayoutManager(this)
    private val adapter = CardAdapter(this, supportFragmentManager)
    private val viewModel: CardViewModel by lazy {
        ViewModelProvider(this).get(CardViewModel::class.java)
    }
    private val playerViewModel: PlayerViewModel by lazy {
        ViewModelProvider(this).get(PlayerViewModel::class.java)
    }
    private val LOCATION_PERMISSION = 999
    private var lat: Double = 0.0
    private var lng: Double = 0.0
    private val locationListener: LocationListener = object: LocationListener {
        override fun onLocationChanged(location: Location?) {
            if (location != null) {
                lat = location.latitude
                lng = location.longitude
                playerViewModel.updateLocation(lat, lng)
            }
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}

        override fun onProviderEnabled(provider: String?) {}

        override fun onProviderDisabled(provider: String?) {}
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed)

        bnvFeed.selectedItemId = R.id.btGlobal
        bnvFeed.setOnNavigationItemSelectedListener { menuItem ->
            when(menuItem.itemId) {
                R.id.btLocal -> {
                    startActivity(Intent("OVERTRACKER_LOCAL_FEED")
                        .addCategory("OVERTRACKER_LOCAL_FEED")
                        .addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT))
                    overridePendingTransition(0, 0)
                }
                R.id.btProfile -> {
                    startActivity(Intent("OVERTRACKER_PROFILE")
                        .addCategory("OVERTRACKER_PROFILE")
                        .addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT))
                    overridePendingTransition(0, 0)
                }
                R.id.btHeroes -> {
                    startActivity(Intent("OVERTRACKER_HERO_LIST")
                        .addCategory("OVERTRACKER_HERO_LIST")
                        .addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT))
                    overridePendingTransition(0, 0)
                }
                R.id.btChat -> {
                    startActivity(Intent("OVERTRACKER_CHAT")
                        .addCategory("OVERTRACKER_CHAT")
                        .addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT))
                    overridePendingTransition(0, 0)
                }
                else -> {
                    val smoothScroller: RecyclerView.SmoothScroller = object: LinearSmoothScroller(this) {
                        override fun getVerticalSnapPreference(): Int {
                            return SNAP_TO_START
                        }
                    }
                    smoothScroller.targetPosition = 0
                    layoutManager.startSmoothScroll(smoothScroller)
                }
            }
            return@setOnNavigationItemSelectedListener false
        }

        srlFeed.setOnRefreshListener { onRefresh() }
        srlFeed.setColorSchemeColors(ContextCompat.getColor(this, R.color.colorPrimary))
        srlFeed.setProgressBackgroundColorSchemeColor(ContextCompat.getColor(this, R.color.colorPrimaryDark))

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                    LOCATION_PERMISSION)
        } else {
            getUserLocation()
        }

        playerViewModel.locationUpdated.observe(this, Observer { result ->
            println("Location updated: $result")
        })

        configureRecyclerView()
        getFeed()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            LOCATION_PERMISSION -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    getUserLocation()
                } else {
                    Toast.makeText(this, getString(R.string.location_denied), Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getUserLocation() {
       val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0, 0.1f, locationListener)
    }

    private fun getFeed() {
        if(showLoadingIcon) feedLoadingContainer.visibility = View.VISIBLE
        isLoading = true
        viewModel.getFeed(page)
    }

    private fun configureRecyclerView() {
        rvFeed.adapter = adapter
        viewModel.cardList.observe(this, Observer { cards ->
            srlFeed.isRefreshing = false
            feedLoadingContainer.visibility = View.GONE
            isLoading = cards.isNullOrEmpty()
            if(refresh) adapter.setCards(cards)
            else adapter.addCards(cards)
        })

        viewModel.error.observe(this, Observer { response ->
            if(!response.status && response.msg != "") {
                srlFeed.isRefreshing = false
                feedLoadingContainer.visibility = View.GONE
                Toast.makeText(this, response.msg, Toast.LENGTH_SHORT).show()
            }
        })

        rvFeed.layoutManager = layoutManager
        rvFeed.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if(dy > 0) {
                    val visibleItemCount = layoutManager.childCount
                    val pastVisibleItem = layoutManager.findFirstCompletelyVisibleItemPosition()
                    val total = adapter.itemCount

                    if(!isLoading && visibleItemCount + pastVisibleItem >= total) {
                        rvFeed.post { getNextPage() }
                    }
                }
            }
        })
    }

    override fun onBackPressed() {
        finish()
        overridePendingTransition(0, 0)
    }

    override fun onResume() {
        super.onResume()
        overridePendingTransition(0, 0)
    }

    private fun onRefresh() {
        page = 1
        refresh = true
        showLoadingIcon = false
        getFeed()
    }

    fun getNextPage() {
        page++
        refresh = false
        showLoadingIcon = true
        getFeed()
    }
}