package studios.eaemenkk.overtracker.interactor

import android.content.Context
import studios.eaemenkk.overtracker.R
import studios.eaemenkk.overtracker.domain.Player
import studios.eaemenkk.overtracker.respository.PlayerRepository
import java.lang.Exception

class PlayerInteractor(context: Context) {
    private val playerRepository = PlayerRepository(context, context.getString(R.string.api_base_url))

    fun playerInfo(authToken: String, tagId: String, callback: (player: Player) -> Unit) {
        playerRepository.playerInfo(authToken, tagId, callback)
    }

    fun followedPlayers(authToken: String, callback: (players: ArrayList<Player>?) -> Unit) {
        playerRepository.followedPlayers(authToken, callback)
    }

    fun createPlayer(authToken: String, tag: String, platform: String, callback: (status: Boolean) -> Unit) {
        val tagFormat = Regex("""^\D\w{2,12}#\d{4,5}$""")
        if(!tagFormat.matches(tag)) {
            throw Exception("Invalid BattleTag.")
        }

        playerRepository.createPlayer(authToken, tag, platform, callback)
    }
}