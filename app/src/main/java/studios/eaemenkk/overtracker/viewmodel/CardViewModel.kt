package studios.eaemenkk.overtracker.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import studios.eaemenkk.overtracker.domain.Card
import studios.eaemenkk.overtracker.interactor.CardInteractor

class CardViewModel(app: Application) : AndroidViewModel(app){
    private val interactor = CardInteractor(app.applicationContext)

    val cardList = MutableLiveData<Array<Card>>()
    val localCardList = MutableLiveData<Array<Card>>()

    fun getFeed(page: Int = 1) {
        interactor.getFeed(page) { cards ->
            cardList.value = cards
        }
    }

    fun getLocalFeed(authToken: String, page: Int = 1) {
        interactor.getLocalFeed(authToken, page) {cards ->
            localCardList.value = cards
        }
    }
}