package studios.eaemenkk.overtracker.interactor

import android.content.Context
import studios.eaemenkk.overtracker.R
import studios.eaemenkk.overtracker.domain.Hero
import studios.eaemenkk.overtracker.domain.HeroDetails
import studios.eaemenkk.overtracker.domain.UserLocation
import studios.eaemenkk.overtracker.respository.retrofit.HeroRepository

class HeroInteractor(context: Context) {
    private val heroRepository =
        HeroRepository(
            context,
            context.getString(R.string.api_base_url)
        )

    suspend fun getHero(heroName: String, callback: (hero: HeroDetails?) -> Unit) {
        heroRepository.getHero(heroName, callback)
    }

    suspend fun getHeroes(callback: (heroes: ArrayList<Hero>) -> Unit) {
        heroRepository.getHeroes(callback)
    }

    fun getMainsPerRegion(hero: String, callback: (location: UserLocation) -> Unit) {
        heroRepository.getMainsPerRegion(hero, callback)
    }
}