package studios.eaemenkk.overtracker.interactor

import android.content.Context
import studios.eaemenkk.overtracker.R
import studios.eaemenkk.overtracker.domain.Player
import studios.eaemenkk.overtracker.respository.PlayerRepository
import java.lang.Exception

class PlayerInteractor(context: Context) {
    private val playerRepository = PlayerRepository(context, context.getString(R.string.api_base_url))

    fun playerInfo(tagId: String, callback: (player: Player?) -> Unit) {
        playerRepository.playerInfo(tagId, callback)
    }

    fun followedPlayers(callback: (players: ArrayList<Player>?) -> Unit) {
        playerRepository.followedPlayers(callback)
    }

    fun createPlayer(tag: String, platform: String, callback: (status: Boolean) -> Unit) {
        if(tag.isEmpty()) throw Exception("Please inform the battletag.")
        if(platform.isEmpty()) throw Exception("Please inform the platform.")
        val tagFormat = Regex("""^\D\w{2,12}#\d{4,5}$""")
        if(!tagFormat.matches(tag)) {
            throw Exception("Invalid BattleTag.")
        }
        playerRepository.createPlayer(tag, platform, callback)
    }

    fun followPlayer(tagId: String, callback: (status: Boolean) -> Unit) {
        playerRepository.followPlayer(tagId, callback)
    }

    fun unfollowPlayer(tagId: String, callback: (status: Boolean) -> Unit) {
        playerRepository.unfollowPlayer(tagId, callback)
    }

    fun isFollowing(tagId: String, callback: (status: Boolean) -> Unit) {
        playerRepository.isFollowing(tagId, callback)
    }
}