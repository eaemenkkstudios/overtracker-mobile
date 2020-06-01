package studios.eaemenkk.overtracker.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import studios.eaemenkk.overtracker.domain.Message
import studios.eaemenkk.overtracker.interactor.ChatInteractor

class ChatViewModel(app: Application) : AndroidViewModel(app) {
    private val interactor = ChatInteractor(app.applicationContext)

    val incomingMsg = MutableLiveData<Message>()

    fun sendMessageToBot(message: String) {
        interactor.sendMessageToBot(message) { msg ->
            if(msg != null) incomingMsg.value = msg
        }
    }
}