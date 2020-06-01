package studios.eaemenkk.overtracker.domain

data class Message(val message: String, val date: Number?) {
    constructor(message: String, date: Number, sender: Boolean) : this(message, date) {
        this.sender = sender
    }

    var sender: Boolean = false
}

data class MessageToServer(val message: String)