package studios.eaemenkk.overtracker.respository

import android.content.Context
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.*
import studios.eaemenkk.overtracker.domain.LocationObject
import studios.eaemenkk.overtracker.domain.NewPlayer
import studios.eaemenkk.overtracker.domain.Player
import studios.eaemenkk.overtracker.domain.UserLocation

interface PlayerService {
    @GET("/info/{tagId}")
    fun playerInfo(
        @Path("tagId") tagId: String
    ): Call<Player>

    @GET("/following")
    fun followedPlayers() : Call<ArrayList<Player>>

    @POST("/followtag")
    fun createPlayer(
        @Body player: NewPlayer
    ) : Call<Void>

    @POST("/followid/{tagId}")
    fun followPlayer(
        @Path("tagId") tagId: String
    ): Call<Void>

    @POST("/unfollowid/{tagId}")
    fun unfollowPlayer(
        @Path("tagId") tagId: String
    ): Call<Void>

    @GET("/isfollowing/{tagId}")
    fun isFollowing(
        @Path("tagId") tagId: String
    ): Call<Void>

    @POST("/location")
    fun updateLocation(
        @Body location: UserLocation
    ): Call<Void>

    @GET("/heroes/{hero}/region")
    fun getMainsPerRegion(
        @Path("hero") hero: String
    ): Call<UserLocation>
}

class PlayerRepository(context: Context, baseUrl: String) : BaseRetrofit(context, baseUrl) {
    private val service = retrofit.create(PlayerService::class.java)

    fun playerInfo(tagId: String, callback: (player: Player?) -> Unit) {
        service.playerInfo(tagId).enqueue(object : Callback<Player> {
            override fun onResponse(call: Call<Player>, response: Response<Player>) {
                val player = response.body()
                if (player != null) {
                    callback(player)
                } else {
                    callback(null)
                }
            }

            override fun onFailure(call: Call<Player>, t: Throwable) {
                callback(null)
            }
        })
    }

    fun followedPlayers(callback: (players: ArrayList<Player>?) -> Unit) {
        service.followedPlayers().enqueue(object : Callback<ArrayList<Player>?> {
            override fun onResponse(call: Call<ArrayList<Player>?>, response: Response<ArrayList<Player>?>) {
                val players = response.body()
                callback(players)
            }

            override fun onFailure(call: Call<ArrayList<Player>?>, t: Throwable) {
                callback(null)
            }
        })
    }

    fun createPlayer(tag: String, platform: String, callback: (status: Boolean) -> Unit) {
        val player = NewPlayer(tag, platform)
        service.createPlayer(player).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if(response.code() <= 201) {
                    callback(true)
                } else {
                    callback(false)
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                callback(false)
            }
        })
    }

    fun followPlayer(tagId: String, callback: (status: Boolean) -> Unit) {
        service.followPlayer(tagId).enqueue(object: Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if(response.code() == 200) {
                    callback(true)
                } else {
                    callback(false)
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                callback(false)
            }
        })
    }

    fun unfollowPlayer(tagId: String, callback: (status: Boolean) -> Unit) {
        service.unfollowPlayer(tagId).enqueue(object: Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if(response.code() == 200) {
                    callback(true)
                } else {
                    callback(false)
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                callback(false)
            }
        })
    }

    fun isFollowing(tagId: String, callback: (status: Boolean) -> Unit) {
        service.isFollowing(tagId).enqueue(object: Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if(response.code() == 200) {
                    callback(true)
                } else {
                    callback(false)
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                callback(false)
            }
        })
    }

    fun updateLocation(lat: Double, lng: Double, callback: (status: Boolean) -> Unit) {
        service.updateLocation(UserLocation(LocationObject(lat, lng))).enqueue(object: Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if(response.code() == 200) {
                    callback(true)
                } else {
                    callback(false)
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                callback(false)
            }
        })
    }

    fun getMainsPerRegion(hero: String, callback: (location: UserLocation) -> Unit) {
        service.getMainsPerRegion(hero).enqueue(object: Callback<UserLocation> {
            override fun onResponse(call: Call<UserLocation>, response: Response<UserLocation>) {
                val location = response.body()
                if (location != null) {
                    callback(location)
                } else {
                    callback(UserLocation(LocationObject(0.0, 0.0)))
                }
            }

            override fun onFailure(call: Call<UserLocation>, t: Throwable) {
                callback(UserLocation(LocationObject(0.0, 0.0)))
            }
        })
    }
}