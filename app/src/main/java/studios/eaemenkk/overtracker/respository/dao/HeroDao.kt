package studios.eaemenkk.overtracker.respository.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import studios.eaemenkk.overtracker.domain.Hero
import studios.eaemenkk.overtracker.domain.HeroDetails

@Dao
interface HeroDao {
    @Query("SELECT * from hero")
    fun getAllHeroes(): List<Hero>

   @Query("SELECT * from herodetails where name = :heroName")
    fun getHeroDetail(heroName: String): List<HeroDetails>

    @Query("DELETE from herodetails where name = :heroName")
    fun deleteHeroDetail(heroName: String)

    @Insert(entity = HeroDetails::class)
    fun insertHeroDetail(hero: HeroDetails)

    @Insert(entity = Hero::class)
    fun insertAllHeroes(vararg heroes: Hero)

    @Query("DELETE from hero")
    fun deleteAllHeroes()
}