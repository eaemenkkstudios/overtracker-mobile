package studios.eaemenkk.overtracker.respository.room

import android.content.Context
import androidx.room.Room
import studios.eaemenkk.overtracker.R
import studios.eaemenkk.overtracker.domain.Hero
import studios.eaemenkk.overtracker.domain.HeroDetails

class HeroRoomRepository(val context: Context) {
    private val db = Room.databaseBuilder(context, AppDatabase::class.java, context.getString(R.string.database_name)).build()

    fun insertAllHeroes(heroes: List<Hero>) {
        return db.heroDao().insertAllHeroes(*heroes.toTypedArray())
    }

    fun getAllHeroes(): List<Hero> {
        return db.heroDao().getAllHeroes()
    }

    fun deleteAllHeroes() {
        return db.heroDao().deleteAllHeroes()
    }

    fun getHeroDetail(heroName: String): List<HeroDetails> {
        return db.heroDao().getHeroDetail(heroName)
    }

    fun deleteHeroDetail(heroName: String) {
        return db.heroDao().deleteHeroDetail(heroName)
    }

    fun insertHeroDetail(hero: HeroDetails) {
        return db.heroDao().insertHeroDetail(hero)
    }

}