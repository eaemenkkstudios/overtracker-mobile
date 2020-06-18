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
import studios.eaemenkk.overtracker.domain.HeroDetails
import studios.eaemenkk.overtracker.domain.LocationObject
import studios.eaemenkk.overtracker.domain.UserLocation
import kotlin.coroutines.coroutineContext

interface HeroService {
    @GET("/heroes/{heroName}")
    fun getHero(
        @Path("heroName") heroName: String
    ): Call<HeroDetails>

    @GET("/heroes")
    fun getHeroes(): Call<ArrayList<Hero>>

    @GET("/heroes/{hero}/region")
    fun getMainsPerRegion(
        @Path("hero") hero: String
    ): Call<UserLocation>
}

class HeroRepository(context: Context, baseUrl: String): BaseRetrofit(context, baseUrl) {
    private val service = retrofit.create(HeroService::class.java)
    private val room = HeroRoomRepository(context)
    suspend fun getHero(heroName: String, callback: (hero: HeroDetails?) -> Unit) {
        coroutineScope {
            val heroDetail = room.getHeroDetail(heroName)
            var sent = false
            if (heroDetail.isNotEmpty()) {
                callback(heroDetail[0])
                sent = true
            }
            service.getHero(heroName).enqueue(object: Callback<HeroDetails?> {
                override fun onResponse(call: Call<HeroDetails?>, response: Response<HeroDetails?>) {
                    GlobalScope.launch {
                        room.deleteHeroDetail(heroName)
                        val hero = response.body()
                        if (hero != null) {
                            room.insertHeroDetail(hero)
                        }
                        if (!sent) {
                            callback(hero)
                        }
                    }
                }

                override fun onFailure(call: Call<HeroDetails?>, t: Throwable) {
                    if (!sent) {
                        callback(null)
                    }
                }
            })
        }
    }

    suspend fun getHeroes(callback: (hero: ArrayList<Hero>) -> Unit) {
        coroutineScope {
            val heroList = room.getAllHeroes()
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
                            room.insertAllHeroes(heroes)
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