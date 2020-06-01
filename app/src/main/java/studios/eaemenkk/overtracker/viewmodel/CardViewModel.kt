package studios.eaemenkk.overtracker.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import studios.eaemenkk.overtracker.R
import studios.eaemenkk.overtracker.domain.Card
import studios.eaemenkk.overtracker.domain.RequestResult
import studios.eaemenkk.overtracker.interactor.CardInteractor
import java.util.*
import kotlin.collections.ArrayList

class CardViewModel(private val app: Application) : AndroidViewModel(app) {
    private val interactor = CardInteractor(app.applicationContext)

    val cardList = MutableLiveData<ArrayList<Card>>()
    val localCardList = MutableLiveData<ArrayList<Card>>()
    val error = MutableLiveData<RequestResult>()

    fun getFeed(page: Int = 1) {
        interactor.getFeed(page) { cards ->
            if(cards == null) {
                error.value = RequestResult(false, app.applicationContext.getString(R.string.could_not_load_feed))
                cardList.value = null
            } else cardList.value = formatCards(cards)
        }
    }

    fun getLocalFeed(page: Int = 1) {
        interactor.getLocalFeed(page) {cards ->
            if(cards == null) {
                error.value = RequestResult(false, app.applicationContext.getString(R.string.could_not_load_feed))
                localCardList.value = null
            } else  localCardList.value = formatCards(cards)
        }
    }

    private fun formatCards(cards: ArrayList<Card>) : ArrayList<Card> {
        cards.forEach { card ->
            val battleTag = card.player?.tag?.split("#")
            card.player?.tag = "${battleTag?.get(0)} "
            card.player?.tagNum = "#${battleTag?.get(1)} "
            card.player?.platform = card.player?.platform?.toUpperCase(Locale.ROOT)
            when(card.cardType) {
                "main_update" -> {
                    card.current?.portrait = when(card.current?.hero) {
                        "wreckingball" -> "https://d1u1mce87gyfbn.cloudfront.net/hero/wrecking-ball/hero-select-portrait.png"
                        "soldier76" -> "https://d1u1mce87gyfbn.cloudfront.net/hero/soldier-76/hero-select-portrait.png"
                        else -> "https://d1u1mce87gyfbn.cloudfront.net/hero/${card.current?.hero}/hero-select-portrait.png"
                    }
                    card.previous?.portrait = when(card.previous?.hero) {
                        "wreckingball" -> "https://d1u1mce87gyfbn.cloudfront.net/hero/wrecking-ball/hero-select-portrait.png"
                        "soldier76" -> "https://d1u1mce87gyfbn.cloudfront.net/hero/soldier-76/hero-select-portrait.png"
                        else -> "https://d1u1mce87gyfbn.cloudfront.net/hero/${card.previous?.hero}/hero-select-portrait.png"
                    }
                }
                "highlight" -> {
                    card.main?.portrait = when(card.main?.current) {
                        "wreckingball" -> "https://d1u1mce87gyfbn.cloudfront.net/hero/wrecking-ball/background-story.jpg"
                        "soldier76" -> "https://d1u1mce87gyfbn.cloudfront.net/hero/soldier-76/background-story.jpg"
                        else -> "https://d1u1mce87gyfbn.cloudfront.net/hero/${card.main?.current}/background-story.jpg"
                    }
                    card.main?.current = when(card.main?.current) {
                        "dva" -> "d.va"
                        "lucio" -> "lúcio"
                        "torbjorn" -> "torbjörn"
                        "wreckingball" -> "wrecking ball"
                        "soldier76" -> "soldier: 76"
                        else -> card.main?.current
                    }
                    card.main?.current = "main ${card.main?.current} "
                    card.sr?.current = "${card.sr?.current} sr"
                }
            }
            card.current?.hero = when(card.current?.hero) {
                "dva" -> "d.va"
                "lucio" -> "lúcio"
                "torbjorn" -> "torbjörn"
                "wreckingball" -> "wrecking ball"
                "soldier76" -> "soldier: 76"
                else -> card.current?.hero
            }
            card.previous?.hero = when(card.previous?.hero) {
                "dva" -> "d.va"
                "lucio" -> "lúcio"
                "torbjorn" -> "torbjörn"
                "wreckingball" -> "wrecking ball"
                "soldier76" -> "soldier: 76"
                else -> card.previous?.hero
            }
            card.main?.time = "${card.main?.time?.split(":")?.get(0)}h"
        }
        return cards
    }
}