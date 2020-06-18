package studios.eaemenkk.overtracker.domain

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Hero (
    @PrimaryKey val name: String,
    @ColumnInfo(name = "role") var role: String,
    @ColumnInfo(name = "img") var img: String?,
    @ColumnInfo(name = "raw_name") var raw_name: String?
)

@Entity
data class HeroDetails (
    @PrimaryKey val name: String,
    @ColumnInfo(name = "role") var role: String,
    @ColumnInfo(name = "img") var img: String?,
    @ColumnInfo(name = "lore") val lore: String?,
    @ColumnInfo(name = "video") var video: String?,
    @ColumnInfo(name = "difficulty") val difficulty: Int?,
    @ColumnInfo(name = "raw_name") var raw_name: String?
)