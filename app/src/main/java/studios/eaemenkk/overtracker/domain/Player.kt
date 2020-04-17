package studios.eaemenkk.overtracker.domain

class Player {
    val id: String? = null
    val lastUpdate: String? = null
    val platform: String? = null
    val tag: String? = null
    val current: Score? = null
    val scores: Array<Score>? = null
    val now: Array<Score>? = null
}

class Score {
    val date: String? = null
    val endorsement: String? = null
    val games: Game? = null
    val main: String? = null
    val rank: Rank? = null
}

class Game {
    val played: Int? = null
    val won: Int? = null
}

class Rank {
    val damage: Ranking? = null
    val support: Ranking? = null
    val tank: Ranking? = null
}

class Ranking {
    val sr: Int? = null
    val img: String? = null
}