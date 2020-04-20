package studios.eaemenkk.overtracker.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import studios.eaemenkk.overtracker.domain.FirebaseResult
import studios.eaemenkk.overtracker.interactor.DatabaseInteractor

class DatabaseViewModel(app: Application) : AndroidViewModel(app) {
    private val interactor = DatabaseInteractor()

    val followStatus = MutableLiveData<FirebaseResult>()
    val unfollowStatus = MutableLiveData<FirebaseResult>()

    fun followPlayer(playerId: String) {
        interactor.followPlayer(playerId) { task ->
                followStatus.value = FirebaseResult(task.isSuccessful, "")
        }
    }

    fun unfollowPlayer(playerId: String) {
        interactor.unfollowPlayer(playerId) { task ->
            unfollowStatus.value = FirebaseResult(task.isSuccessful, "")
        }
    }

    fun isFollowing(playerId: String) {
        interactor.isFollowing(playerId) { following ->
            if(following) {
                followStatus.value = FirebaseResult(true, "")
            } else {
                unfollowStatus.value = FirebaseResult(true, "")
            }
        }
    }
}