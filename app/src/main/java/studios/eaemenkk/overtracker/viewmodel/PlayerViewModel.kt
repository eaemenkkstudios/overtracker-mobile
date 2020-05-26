package studios.eaemenkk.overtracker.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import studios.eaemenkk.overtracker.domain.Player
import studios.eaemenkk.overtracker.domain.RequestResult
import studios.eaemenkk.overtracker.interactor.PlayerInteractor
import java.lang.Exception
import kotlin.math.floor

class PlayerViewModel(app: Application) : AndroidViewModel(app) {
    private val interactor = PlayerInteractor(app.applicationContext)

    val playerDetails = MutableLiveData<Player>()
    val playerList = MutableLiveData<ArrayList<Player>>()
    val created = MutableLiveData<RequestResult>()

    fun playerInfo(authToken: String, tagId: String) {
        interactor.playerInfo(authToken, tagId) { player ->
            val battleTag = player.tag?.split("#")
            player.tag = "${battleTag?.get(0)} "
            player.tagNum = "#${battleTag?.get(1)} "
            player.now?.rank?.damage?.sr = "${player.now?.rank?.damage?.sr} "
            player.now?.rank?.support?.sr = "${player.now?.rank?.support?.sr} "
            player.now?.rank?.tank?.sr = "${player.now?.rank?.tank?.sr} "
             "https://d1u1mce87gyfbn.cloudfront.net/hero/${player.now?.main?.hero}/hero-select-portrait.png"
            player.now?.portrait = when (player.now?.main?.hero) {
                    "wreckingball" -> "https://d1u1mce87gyfbn.cloudfront.net/hero/wrecking-ball/hero-select-portrait.png"
                    "soldier76" -> "https://d1u1mce87gyfbn.cloudfront.net/hero/soldier-76/hero-select-portrait.png"
                    else -> "https://d1u1mce87gyfbn.cloudfront.net/hero/${player.now?.main?.hero}/hero-select-portrait.png"
            }
            player.now?.main?.hero = when(player.now?.main?.hero) {
                "dva" -> "d.va"
                "lucio" -> "lúcio"
                "torbjorn" -> "torbjörn"
                "wreckingball" -> "wrecking ball"
                "soldier76" -> "soldier: 76"
                else -> player.now?.main?.hero
            }
            player.now?.main?.time = "${player.now?.main?.time?.split(":")?.get(0)}h"
            player.now?.main?.hero = "${player.now?.main?.hero} "
            player.platform = player.platform?.toUpperCase()
            player.scores?.forEach { score ->
                score.rank?.damage?.sr = "${score.rank?.damage?.sr} "
                score.rank?.support?.sr = "${score.rank?.support?.sr} "
                score.rank?.tank?.sr = "${score.rank?.tank?.sr} "
                score.date = timestampToTimeInterval(score.date.toString())
            }
            playerDetails.value = player
        }
    }

    fun followedPlayers(authToken: String) {
        interactor.followedPlayers(authToken) { players ->
            players?.forEach { player ->
                val battleTag = player.tag?.split("#")
                player.tag = "${battleTag?.get(0)} "
                player.tagNum = "#${battleTag?.get(1)} "
                player.platform = player.platform?.toUpperCase()
            }
            playerList.value = players
        }
    }

    fun createPlayer(authToken: String, tag: String, platform: String = "pc") {
        interactor.createPlayer(authToken, tag, platform) { status ->
            if(status) {
                created.value = RequestResult(status, "Player added")
            } else {
                created.value = RequestResult(status, "Failed to add player")
            }

        }
    }

    private fun timestampToTimeInterval(timestamp: String): String {
        val currentTimestamp = System.currentTimeMillis() / 1000
        val timestampDiff = currentTimestamp - (timestamp.toLong() / 1000)

        var word = "time unit"
        var divider = 1

        if(timestampDiff < 60){
            word = "second"
        } else if(timestampDiff < 3600) {
            word = "minute"
            divider = 60
        }
        else if (timestampDiff < 86400) {
            word = "hour"
            divider = 3600
        }
        else if (timestampDiff < 604800) {
            word = "day"
            divider = 86400
        }
        else if (timestampDiff < 2419200) {
            word = "week"
            divider = 604800
        }
        else if (timestampDiff < 29030400) {
            word = "month"
            divider = 2419200
        }
        else {
            word = "year"
            divider = 29030400
        }

        val unit = floor(timestampDiff.toDouble()/divider).toInt()

         return if(unit <= 1){
             "$unit $word ago"
         } else {
             "$unit ${word}s ago"
         }

    }
}