package studios.eaemenkk.overtracker.domain

data class UserLocation (
    val location: LocationObject

)

data class LocationObject (
    val lat: Double,
    val lng: Double
)