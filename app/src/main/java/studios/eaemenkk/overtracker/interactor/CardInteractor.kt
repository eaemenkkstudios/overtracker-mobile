package studios.eaemenkk.overtracker.interactor

import android.content.Context
import studios.eaemenkk.overtracker.R
import studios.eaemenkk.overtracker.domain.Card
import studios.eaemenkk.overtracker.respository.retrofit.CardRepository

class CardInteractor(context: Context) {
    private val cardRepository =
        CardRepository(
            context,
            context.getString(R.string.api_base_url)
        )

    fun getFeed(page: Int = 1, callback: (cards: ArrayList<Card>?) -> Unit) {
        cardRepository.getFeed(page, callback)
    }

    fun getLocalFeed(page: Int = 1, callback: (cards: ArrayList<Card>?) -> Unit) {
        cardRepository.getLocalFeed(page, callback)
    }
}