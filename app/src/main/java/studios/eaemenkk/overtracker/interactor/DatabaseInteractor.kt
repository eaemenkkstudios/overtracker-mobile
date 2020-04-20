package studios.eaemenkk.overtracker.interactor

import com.google.android.gms.tasks.Task
import studios.eaemenkk.overtracker.domain.FirebaseResult
import studios.eaemenkk.overtracker.respository.DatabaseRepository

class DatabaseInteractor {
    private val databaseRepository = DatabaseRepository()

    fun followPlayer(playerId: String, callback: (result: Task<Void>) -> Unit) {
        databaseRepository.followPlayer(playerId, callback)
    }

    fun unfollowPlayer(playerId: String, callback: (result: Task<Void>) -> Unit) {
        databaseRepository.unfollowPlayer(playerId, callback)
    }

    fun isFollowing(playerId: String, callback: (result: Boolean) -> Unit) {
        databaseRepository.isFollowing(playerId, callback)
    }
}