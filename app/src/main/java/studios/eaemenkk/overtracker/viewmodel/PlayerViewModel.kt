package studios.eaemenkk.overtracker.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import studios.eaemenkk.overtracker.R
import studios.eaemenkk.overtracker.domain.Player
import studios.eaemenkk.overtracker.domain.RequestResult
import studios.eaemenkk.overtracker.domain.UserLocation
import studios.eaemenkk.overtracker.interactor.PlayerInteractor
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.math.floor

class PlayerViewModel(private val app: Application) : AndroidViewModel(app) {
    private val interactor = PlayerInteractor(app.applicationContext)

    val playerDetails = MutableLiveData<Player>()
    val playerList = MutableLiveData<ArrayList<Player>>()
    val created = MutableLiveData<RequestResult>()
    val followed = MutableLiveData<Boolean>()
    val unfollowed = MutableLiveData<Boolean>()
    val locationUpdated = MutableLiveData<Boolean>()
    val heroLocation = MutableLiveData<UserLocation>()

    fun playerInfo(tagId: String) {
        interactor.playerInfo(tagId) { player ->
            if(player != null) {
                val battleTag = player.tag?.split("#")
                player.tag = "${battleTag?.get(0)} "
                player.tagNum = "#${battleTag?.get(1)} "
                player.platform = player.platform?.toUpperCase(Locale.ROOT)
                player.now?.rank?.damage?.sr = "${player.now?.rank?.damage?.sr} "
                player.now?.rank?.support?.sr = "${player.now?.rank?.support?.sr} "
                player.now?.rank?.tank?.sr = "${player.now?.rank?.tank?.sr} "
                player.now?.portrait = when (player.now?.main?.hero) {
                    "wreckingball" -> "https://d1u1mce87gyfbn.cloudfront.net/hero/wrecking-ball/hero-select-portrait.png"
                    "soldier76" -> "https://d1u1mce87gyfbn.cloudfront.net/hero/soldier-76/hero-select-portrait.png"
                    else -> "https://d1u1mce87gyfbn.cloudfront.net/hero/${player.now?.main?.hero}/hero-select-portrait.png"
                }
                player.now?.main?.friendlyHero = when(player.now?.main?.hero) {
                    "wreckingball" -> "wrecking-ball"
                    "soldier76" -> "soldier-76"
                    else -> player.now?.main?.hero
                }
                player.now?.main?.hero = when (player.now?.main?.hero) {
                    "dva" -> "d.va"
                    "lucio" -> "lúcio"
                    "torbjorn" -> "torbjörn"
                    "wreckingball" -> "wrecking ball"
                    "soldier76" -> "soldier: 76"
                    else -> player.now?.main?.hero
                }
                player.now?.main?.time = "${player.now?.main?.time?.split(":")?.get(0)}h"
                player.now?.main?.hero = "${player.now?.main?.hero} "
                player.scores?.forEach { score ->
                    score.rank?.damage?.sr = "${score.rank?.damage?.sr} "
                    score.rank?.support?.sr = "${score.rank?.support?.sr} "
                    score.rank?.tank?.sr = "${score.rank?.tank?.sr} "
                    score.date = timestampToTimeInterval(score.date.toString())
                }
                playerDetails.value = player
            }
        }
    }

    fun followedPlayers() {
        interactor.followedPlayers { players ->
            if(players != null) {
                players.forEach { player ->
                    val battleTag = player.tag?.split("#")
                    player.tag = "${battleTag?.get(0)} "
                    player.tagNum = "#${battleTag?.get(1)} "
                    player.platform = player.platform?.toUpperCase(Locale.ROOT)
                }
                playerList.value = players
            }
        }
    }

    fun createPlayer(tag: String, platform: String = "pc") {
        interactor.createPlayer(tag, platform) { status ->
            if(status) {
                created.value = RequestResult(status, app.applicationContext.getString(R.string.player_added))
            } else {
                created.value = RequestResult(status, app.applicationContext.getString(R.string.add_player_failed))
            }

        }
    }

    fun followPlayer(tagId: String) {
        interactor.followPlayer(tagId) { result -> followed.value = result }
    }

    fun unfollowPlayer(tagId: String) {
        interactor.unfollowPlayer(tagId) { result -> unfollowed.value = result }
    }

    fun isFollowing(tagId: String) {
        interactor.isFollowing(tagId) { result ->
            if(result) followed.value = true
            else unfollowed.value = true
        }
    }

    fun updateLocation(lat: Double, lng: Double) {
        interactor.updateLocation(lat, lng) { result -> locationUpdated.value = result }
    }

    fun getMainsPerRegion(hero: String) {
        interactor.getMainsPerRegion(hero) { location -> heroLocation.value = location }
    }

    private fun timestampToTimeInterval(timestamp: String): String {
        val currentTimestamp = System.currentTimeMillis() / 1000
        val timestampDiff = currentTimestamp - (timestamp.toLong() / 1000)

        val ago = app.applicationContext.getString(R.string.ago)
        val word: String
        var divider = 1

        if(timestampDiff < 60){
            word = app.applicationContext.getString(R.string.second)
        } else if(timestampDiff < 3600) {
            word = app.applicationContext.getString(R.string.minute)
            divider = 60
        }
        else if (timestampDiff < 86400) {
            word = app.applicationContext.getString(R.string.hour)
            divider = 3600
        }
        else if (timestampDiff < 604800) {
            word = app.applicationContext.getString(R.string.day)
            divider = 86400
        }
        else if (timestampDiff < 2419200) {
            word = app.applicationContext.getString(R.string.week)
            divider = 604800
        }
        else if (timestampDiff < 29030400) {
            word = app.applicationContext.getString(R.string.month)
            divider = 2419200
        }
        else {
            word = app.applicationContext.getString(R.string.year)
            divider = 29030400
        }

        val unit = floor(timestampDiff.toDouble()/divider).toInt()

         return if(unit <= 1){
             "$unit $word $ago"
         } else {
             "$unit ${word}s $ago"
         }

    }
}