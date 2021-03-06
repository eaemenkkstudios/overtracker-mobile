package studios.eaemenkk.overtracker.interactor

import android.content.Context
import studios.eaemenkk.overtracker.R
import studios.eaemenkk.overtracker.domain.Message
import studios.eaemenkk.overtracker.respository.retrofit.ChatRepository
import java.lang.Exception

class ChatInteractor(context: Context) {
    private val repository =
        ChatRepository(
            context,
            context.getString(R.string.api_base_url)
        )

    fun sendMessageToBot(message: String, callback: (message: Message?) -> Unit) {
        if (message.isEmpty()) throw Exception("")
        repository.sendMessageToBot(message, callback)
    }
}