package studios.eaemenkk.overtracker.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import studios.eaemenkk.overtracker.domain.Hero
import studios.eaemenkk.overtracker.interactor.HeroInteractor

class HeroViewModel(app: Application): AndroidViewModel(app) {
    private val interactor = HeroInteractor(app.applicationContext)

    val heroInfo = MutableLiveData<Hero>()
    val heroList = MutableLiveData<ArrayList<Hero>>()

    fun getHero(heroName: String) {
        interactor.getHero(heroName) {hero ->
            if(hero != null) {
                hero.friendlyName = when(hero.name) {
                    "dva" -> "d.va"
                    "lucio" -> "lúcio"
                    "torbjorn" -> "torbjörn"
                    "wrecking-ball" -> "wrecking ball"
                    "soldier-76" -> "soldier: 76"
                    else -> hero.name
                }
                heroInfo.value = hero
            }
        }
    }

    fun getHeroes() {
        interactor.getHeroes { heroes ->
            heroes.forEach { hero ->
                hero.friendlyName = when(hero.name) {
                    "dva" -> "d.va"
                    "lucio" -> "lúcio"
                    "torbjorn" -> "torbjörn"
                    "wrecking-ball" -> "wrecking ball"
                    "soldier-76" -> "soldier: 76"
                    else -> hero.name
                }
            }
            heroList.value = heroes
        }
    }
}