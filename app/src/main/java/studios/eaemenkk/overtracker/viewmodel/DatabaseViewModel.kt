package studios.eaemenkk.overtracker.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import studios.eaemenkk.overtracker.domain.RequestResult
import studios.eaemenkk.overtracker.interactor.DatabaseInteractor

class DatabaseViewModel(app: Application) : AndroidViewModel(app) {
    private val interactor = DatabaseInteractor()

    val followStatus = MutableLiveData<RequestResult>()
    val unfollowStatus = MutableLiveData<RequestResult>()

    fun followPlayer(playerId: String) {
        interactor.followPlayer(playerId) { task ->
                followStatus.value = RequestResult(task.isSuccessful, "")
        }
    }

    fun unfollowPlayer(playerId: String) {
        interactor.unfollowPlayer(playerId) { task ->
            unfollowStatus.value = RequestResult(task.isSuccessful, "")
        }
    }

    fun isFollowing(playerId: String) {
        interactor.isFollowing(playerId) { following ->
            if(following) {
                followStatus.value = RequestResult(true, "")
            } else {
                unfollowStatus.value = RequestResult(true, "")
            }
        }
    }
}