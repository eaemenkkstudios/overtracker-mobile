package studios.eaemenkk.overtracker.domain

open class Card {
 open val cardType: String? = null
 val role: String? = null
 val date: String? = null
 val player: Player? = null
 val sr: Update? = null
 val endorsement: Update? = null
 val main: Update? = null
 val winrate: Update? = null
 val previous: Update? = null
 val current: Update? = null
}

class Update {
 val previous: String? = null
 var current: String? = null
 val slope: String? = null
 var time: String? = null
 var hero: String? = null
 val role: String? = null
 var portrait: String? = null
}

class AdCard: Card() {
 override val cardType: String = "ad"
}