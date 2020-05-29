package studios.eaemenkk.overtracker.domain

open class Player {
    val id: String? = null
    val lastUpdate: String? = null
    var platform: String? = null
    var tag: String? = null
    var tagNum: String? = null
    val current: Score? = null
    val scores: Array<Score>? = null
    val now: Score? = null
    val portrait: String? = null
}

class Score {
    var date: String? = null
    val endorsement: String? = null
    val games: Game? = null
    val main: Score? = null
    val rank: Rank? = null
    val role: String? = null
    var hero: String? = null
    var portrait: String? = null
    var time: String? = null
}

class Game {
    val played: String? = null
    val won: String? = null
}

class Rank {
    val damage: Ranking? = null
    val support: Ranking? = null
    val tank: Ranking? = null
}

class Ranking {
    var sr: String? = null
    val img: String? = null
}

data class NewPlayer (val tag: String, val platform: String)

class AdPlayer: Player()