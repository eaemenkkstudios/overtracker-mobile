package studios.eaemenkk.overtracker.respository

import android.content.Context
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import studios.eaemenkk.overtracker.domain.Hero

interface HeroService {
    @GET("/heroes/{heroName}")
    fun getHero(
        @Path("heroName") heroName: String
    ): Call<Hero>

    @GET("/heroes")
    fun getHeroes(): Call<ArrayList<Hero>>
}

class HeroRepository(context: Context, baseUrl: String): BaseRetrofit(context, baseUrl) {
    private val service = retrofit.create(HeroService::class.java)

    fun getHero(heroName: String, callback: (hero: Hero?) -> Unit) {
        service.getHero(heroName).enqueue(object: Callback<Hero?> {
            override fun onResponse(call: Call<Hero?>, response: Response<Hero?>) {
                val player = response.body()
                callback(player)
            }

            override fun onFailure(call: Call<Hero?>, t: Throwable) {
                callback(null)
            }
        })
    }

    fun getHeroes(callback: (hero: ArrayList<Hero>) -> Unit) {
        service.getHeroes().enqueue(object: Callback<ArrayList<Hero>> {
            override fun onResponse(call: Call<ArrayList<Hero>>, response: Response<ArrayList<Hero>>) {
                val players = response.body()
                if(players != null) {
                    callback(players)
                } else {
                    callback(ArrayList())
                }
            }
            override fun onFailure(call: Call<ArrayList<Hero>>, t: Throwable) {
                callback(ArrayList())
            }
        })
    }
}