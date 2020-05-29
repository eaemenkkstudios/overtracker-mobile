package studios.eaemenkk.overtracker.respository

import android.content.Context
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.*
import studios.eaemenkk.overtracker.domain.NewPlayer
import studios.eaemenkk.overtracker.domain.Player
import java.lang.Exception

interface PlayerService {
    @GET("/info/{tagId}")
    fun playerInfo(
        @Path("tagId") tagId: String
    ): Call<Player>

    @GET("/following")
    fun followedPlayers() : Call<ArrayList<Player>>

    @POST("/follow")
    fun createPlayer(
        @Body player: NewPlayer
    ) : Call<Void>
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
                throw Exception("Failed to load player info, please try again...")
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
                throw Exception("Failed to load players, please try again...")
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
}