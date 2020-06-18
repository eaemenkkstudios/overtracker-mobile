package studios.eaemenkk.overtracker.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import studios.eaemenkk.overtracker.domain.Hero
import studios.eaemenkk.overtracker.domain.HeroDetails
import studios.eaemenkk.overtracker.domain.UserLocation
import studios.eaemenkk.overtracker.interactor.HeroInteractor

class HeroViewModel(app: Application): AndroidViewModel(app) {
    private val interactor = HeroInteractor(app.applicationContext)

    val heroInfo = MutableLiveData<HeroDetails>()
    val heroList = MutableLiveData<ArrayList<Hero>>()
    val heroLocation = MutableLiveData<UserLocation>()

    suspend fun getHero(heroName: String) {
        interactor.getHero(heroName) {hero ->
            if(hero != null) {
                hero.img = "https://static.playoverwatch.com/img/pages/hero-detail/staticposter-733c75265d.gif"
                hero.video = "https://d1u1mce87gyfbn.cloudfront.net/hero/${hero.name}/intro-video.webm"
                hero.friendlyName = "${hero.friendlyName} "
                hero.role = "${hero.role} "
                heroInfo.postValue(hero)
            }
        }
    }

    suspend fun getHeroes() {
        interactor.getHeroes { heroes -> heroList.postValue(heroes) }
    }

    fun getMainsPerRegion(hero: String) {
        interactor.getMainsPerRegion(hero) { location -> heroLocation.value = location }
    }
}