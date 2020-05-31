package studios.eaemenkk.overtracker.domain

data class Hero (
    val name: String,
    val role: String,
    var img: String?,
    val lore: String?,
    val difficulty: Int?,
    var friendlyName: String?
)