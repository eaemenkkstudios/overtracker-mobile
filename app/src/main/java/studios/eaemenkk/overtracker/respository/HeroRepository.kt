package studios.eaemenkk.overtracker.respository

import android.content.Context
import android.os.AsyncTask
import android.os.Handler
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import studios.eaemenkk.overtracker.domain.Hero
import kotlin.coroutines.coroutineContext

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
    private val room = HeroRoomRepository(context)
    fun getHero(heroName: String, callback: (hero: Hero?) -> Unit) {
        service.getHero(heroName).enqueue(object: Callback<Hero?> {
            override fun onResponse(call: Call<Hero?>, response: Response<Hero?>) {
                val hero = response.body()
                callback(hero)
            }

            override fun onFailure(call: Call<Hero?>, t: Throwable) {
                callback(null)
            }
        })
    }

    suspend fun getHeroes(callback: (hero: ArrayList<Hero>) -> Unit) {
        coroutineScope {
            val heroList = room.getHeroes()
            var sent = false
            if (heroList.isNotEmpty()) {
                callback(heroList as ArrayList<Hero>)
                sent = true
            }

            service.getHeroes().enqueue(object: Callback<ArrayList<Hero>> {
                override fun onResponse(call: Call<ArrayList<Hero>>, response: Response<ArrayList<Hero>>) {
                    val heroes = response.body()
                    if(heroes != null) {
                        GlobalScope.launch {
                            room.deleteAllHeroes()
                            room.insertHeroes(heroes)
                            if (!sent) {
                                callback(heroes)
                            }
                        }
                    } else {
                        if (!sent) {
                            callback(ArrayList())
                        }
                    }
                }
                override fun onFailure(call: Call<ArrayList<Hero>>, t: Throwable) {
                    if (!sent) {
                        callback(ArrayList())
                    }
                }
            })

        }
    }
}