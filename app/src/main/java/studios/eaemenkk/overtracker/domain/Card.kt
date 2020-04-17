package studios.eaemenkk.overtracker.domain

open class Card {
 val type: String? = null
 val role: String? = null
 val date: String? = null
 val player: Player? = null
 val sr: Update? = null
 val endorsement: Update? = null
 val main: Update? = null
 val winrate: Update? = null
}

class Update {
 val previous: Int? = null
 val current: Int? = null
 val slope: String? = null
 val time: String? = null
}