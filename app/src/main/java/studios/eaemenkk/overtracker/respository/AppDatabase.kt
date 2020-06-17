package studios.eaemenkk.overtracker.respository

import androidx.room.Database
import androidx.room.RoomDatabase
import studios.eaemenkk.overtracker.domain.Hero

@Database(entities = [Hero::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun heroDao(): HeroDao
}