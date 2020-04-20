package studios.eaemenkk.overtracker.interactor

import android.content.Context
import studios.eaemenkk.overtracker.R
import studios.eaemenkk.overtracker.domain.Card
import studios.eaemenkk.overtracker.respository.CardRepository

class CardInteractor(context: Context) {
    private val cardRepository = CardRepository(context, context.getString(R.string.api_base_url))

    fun getFeed(page: Int = 1, callback: (cards: Array<Card>) -> Unit) {
        cardRepository.getFeed(page, callback)
    }

    fun getLocalFeed(authToken: String, page: Int = 1, callback: (cards: Array<Card>) -> Unit) {
        cardRepository.getLocalFeed(authToken, page, callback)
    }
}