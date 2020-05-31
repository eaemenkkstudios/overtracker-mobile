package studios.eaemenkk.overtracker.domain

data class Hero (
    val name: String,
    var role: String,
    var img: String?,
    var video: String?,
    val lore: String?,
    val difficulty: Int?,
    var friendlyName: String?
)