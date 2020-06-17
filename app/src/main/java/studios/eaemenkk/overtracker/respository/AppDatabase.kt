package studios.eaemenkk.overtracker.respository

import androidx.room.Database
import androidx.room.RoomDatabase
import studios.eaemenkk.overtracker.domain.Hero
import studios.eaemenkk.overtracker.domain.HeroDetails

@Database(entities = [Hero::class, HeroDetails::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun heroDao(): HeroDao
}