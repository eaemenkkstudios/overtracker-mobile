package studios.eaemenkk.overtracker.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import studios.eaemenkk.overtracker.domain.Player
import studios.eaemenkk.overtracker.interactor.PlayerInteractor

class PlayerViewModel(val app: Application) : AndroidViewModel(app) {
    private val interactor = PlayerInteractor(app.applicationContext)

    val playerDetails = MutableLiveData<Player>()
    val playerList = MutableLiveData<Array<Player>>()
    val created = MutableLiveData<Boolean>()

    fun playerInfo(authToken: String, tagId: String) {
        interactor.playerInfo(authToken, tagId) { player ->
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