package studios.eaemenkk.overtracker.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import studios.eaemenkk.overtracker.domain.Player
import studios.eaemenkk.overtracker.interactor.PlayerInteractor

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
                score.rank?.damage?.sr = "${player.now?.rank?.damage?.sr} "
                score.rank?.support?.sr = "${player.now?.rank?.support?.sr} "
                score.rank?.tank?.sr = "${player.now?.rank?.tank?.sr} "
            }
            playerDetails.value = player
        }
    }

    fun followedPlayers(authToken: String) {
        interactor.followedPlayers(authToken) { players ->
            playerList.value = players
        }
    }

    fun createPlayer(authToken: String, tag: String, platform: String) {
        interactor.createPlayer(authToken, tag, platform) { status ->
            created.value = status
        }
    }
}