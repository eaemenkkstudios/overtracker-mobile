package studios.eaemenkk.overtracker.respository

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import studios.eaemenkk.overtracker.domain.Hero

@Dao
interface HeroDao {
    @Query("SELECT * from hero")
    fun getAll(): List<Hero>

    @Insert
    fun insertAll(vararg heroes: Hero)

    @Query("DELETE from hero")
    fun deleteAll()
}