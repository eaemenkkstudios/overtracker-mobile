package studios.eaemenkk.overtracker.respository

import android.content.Context
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create
import retrofit2.http.*
import studios.eaemenkk.overtracker.domain.Player

interface PlayerService {

    @GET("/info")
    fun playerInfo(
        @Header("Authorization") authToken: String,
        @Path("tagId") tagId: String
    ): Call<Player>

    @GET("/following")
    fun followedPlayers(
        @Header("Authorization") authToken: String
    ) : Call<Array<Player>>

    @POST("/create")
    fun createPlayer(
        @Header("Authorization") authToken: String,
        @Body tag: String,
        @Body platform: String
    ) : Call<Void>
}

class PlayerRepository(context: Context, baseUrl: String) : BaseRetrofit(context, baseUrl) {
    private val service = retrofit.create(PlayerService::class.java)

    fun playerInfo(authToken: String, tagId: String, callback: (player: Player) -> Unit) {
        service.playerInfo(authToken, tagId).enqueue(object : Callback<Player> {
            override fun onResponse(call: Call<Player>, response: Response<Player>) {
                val player = response.body()
                if (player != null) {
                    callback(player)
                } else {
                    callback(Player())
                }
            }

            override fun onFailure(call: Call<Player>, t: Throwable) {
                callback(Player())
            }
        })
    }

    fun followedPlayers(authToken: String, callback: (players: Array<Player>) -> Unit) {
        service.followedPlayers(authToken).enqueue(object : Callback<Array<Player>> {
            override fun onResponse(call: Call<Array<Player>>, response: Response<Array<Player>>) {
                val players = response.body()
                if(players != null) {
                    callback(players)
                } else {
                    callback(arrayOf())
                }
            }

            override fun onFailure(call: Call<Array<Player>>, t: Throwable) {
                callback(arrayOf())
            }
        })
    }

    fun createPlayer(authToken: String, tag: String, platform: String, callback: (status: Boolean) -> Unit) {
        service.createPlayer(authToken, tag, platform).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if(response.code() == 201) {
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