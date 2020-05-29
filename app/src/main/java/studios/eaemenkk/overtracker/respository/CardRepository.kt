package studios.eaemenkk.overtracker.respository

import android.content.Context
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import studios.eaemenkk.overtracker.domain.Card

interface CardService {
    @GET("/feed/global")
    fun getFeed(
        @Query("page") page: Int = 1
    ): Call<ArrayList<Card>>

    @GET("/feed/local")
    fun getLocalFeed(
        @Query("page") page: Int = 1
    ): Call<ArrayList<Card>>
}

class CardRepository (context: Context, baseUrl: String) : BaseRetrofit(context, baseUrl) {
    private val service = retrofit.create(CardService::class.java)

    fun getFeed(page: Int = 1, callback: (cards: ArrayList<Card>?) -> Unit) {
        service.getFeed(page).enqueue(object: Callback<ArrayList<Card>> {
            override fun onResponse(call: Call<ArrayList<Card>>, response: Response<ArrayList<Card>>) {
                val cards = response.body()
                callback(cards)
            }

            override fun onFailure(call: Call<ArrayList<Card>>, t: Throwable) {
                callback(null)
            }
        })
    }

    fun getLocalFeed(page: Int = 1, callback: (cards: ArrayList<Card>?) -> Unit) {
        service.getLocalFeed(page).enqueue(object: Callback<ArrayList<Card>> {
            override fun onResponse(call: Call<ArrayList<Card>>, response: Response<ArrayList<Card>>) {
                val cards = response.body()
                callback(cards)
            }

            override fun onFailure(call: Call<ArrayList<Card>>, t: Throwable) {
                callback(null)
            }
        })
    }
}