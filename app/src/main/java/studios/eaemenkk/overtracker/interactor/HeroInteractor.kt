package studios.eaemenkk.overtracker.interactor

import android.content.Context
import studios.eaemenkk.overtracker.R
import studios.eaemenkk.overtracker.domain.Hero
import studios.eaemenkk.overtracker.respository.HeroRepository

class HeroInteractor(context: Context) {
    private val heroRepository = HeroRepository(context, context.getString(R.string.api_base_url))

    fun getHero(heroName: String, callback: (hero: Hero?) -> Unit) {
        heroRepository.getHero(heroName, callback)
    }

    suspend fun getHeroes(callback: (heroes: ArrayList<Hero>) -> Unit) {
        heroRepository.getHeroes(callback)
    }
}