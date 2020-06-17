package studios.eaemenkk.overtracker.respository

import android.content.Context
import androidx.room.Room
import studios.eaemenkk.overtracker.domain.Hero

class HeroRoomRepository(val context: Context) {
    private val db = Room.databaseBuilder(context, AppDatabase::class.java, "overtracker-db").build()

    fun insertHeroes(heroes: List<Hero>) {
        db.heroDao().insertAll(*heroes.toTypedArray())
    }

    fun getHeroes(): List<Hero> {
        return db.heroDao().getAll()
    }

    fun deleteAllHeroes() {
        return db.heroDao().deleteAll()
    }
}