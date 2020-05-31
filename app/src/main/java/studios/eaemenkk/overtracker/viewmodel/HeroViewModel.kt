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
                // hero.img = "https://d1u1mce87gyfbn.cloudfront.net/hero/${hero.name}/full-portrait.png"
                hero.img = "https://d1u1mce87gyfbn.cloudfront.net/hero/${hero.name}/background-story.jpg"
                hero.video = "https://d1u1mce87gyfbn.cloudfront.net/hero/${hero.name}/idle-video.mp4"
                hero.friendlyName = "${hero.friendlyName} "
                hero.role = "${hero.role} "
                heroInfo.value = hero
            }
        }
    }

    fun getHeroes() {
        interactor.getHeroes { heroes -> heroList.value = heroes }
    }
}