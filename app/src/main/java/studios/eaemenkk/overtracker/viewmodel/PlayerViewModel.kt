package studios.eaemenkk.overtracker.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import studios.eaemenkk.overtracker.domain.Player
import studios.eaemenkk.overtracker.interactor.PlayerInteractor
import kotlin.math.floor

class PlayerViewModel(app: Application) : AndroidViewModel(app) {
    private val interactor = PlayerInteractor(app.applicationContext)

    val playerDetails = MutableLiveData<Player>()
    val playerList = MutableLiveData<Array<Player>>()
    val created = MutableLiveData<Boolean>()

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
                    else -> "https://d1u1mce87gyfbn.cloudfront.net/hero/${player.now?.main?.hero}/hero-select-portrait.png"
            }
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
            players.forEach { player ->
                val battleTag = player.tag?.split("#")
                player.tag = "${battleTag?.get(0)} "
                player.tagNum = "#${battleTag?.get(1)} "
                player.platform = player.platform?.toUpperCase()
            }
            playerList.value = players
        }
    }

    fun createPlayer(authToken: String, tag: String, platform: String) {
        interactor.createPlayer(authToken, tag, platform) { status ->
            created.value = status
        }
    }

    private fun timestampToTimeInterval(timestamp: String): String {
        val currentTimestamp = System.currentTimeMillis() / 1000
        val timestampDiff = currentTimestamp - (timestamp.toLong() / 1000)

        return if(timestampDiff < 60) "$timestampDiff second(s) ago"
        else if(timestampDiff < 3600) "${floor(timestampDiff / 60.0).toInt()} minute(s) ago"
        else if (timestampDiff < 86400) "${floor(timestampDiff / 3600.0).toInt()} hour(s) ago"
        else if (timestampDiff < 604800) "${floor(timestampDiff / 86400.0).toInt()} day(s) ago"
        else if (timestampDiff < 18144000) "${floor(timestampDiff / 604800.0).toInt()} week(s) ago"
        else if (timestampDiff < 31536000) "${floor(timestampDiff / 18144000.0).toInt()} month(s) ago"
        else "${floor(timestampDiff / 31536000.0).toInt()} year(s) ago"

    }
}