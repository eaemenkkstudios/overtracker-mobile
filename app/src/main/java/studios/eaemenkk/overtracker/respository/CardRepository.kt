package studios.eaemenkk.overtracker.respository

import android.content.Context
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Query
import studios.eaemenkk.overtracker.domain.Card

interface CardService {
    @GET("/feed/global")
    fun getFeed(
        @Query("page") page: Int = 1
    ): Call<Array<Card>>
}

class CardRepository (context: Context, baseUrl: String) : BaseRetrofit(context, baseUrl) {
    private val service = retrofit.create(CardService::class.java)

    fun getFeed(page: Int = 1, callback: (cards: Array<Card>) -> Unit) {
        service.getFeed(page).enqueue(object: Callback<Array<Card>> {
            override fun onResponse(call: Call<Array<Card>>, response: Response<Array<Card>>) {
                val cards = response.body()
                if(cards != null) {
                    callback(cards)
                } else {
                    callback(arrayOf(Card()))
                }
            }

            override fun onFailure(call: Call<Array<Card>>, t: Throwable) {
                callback(arrayOf(Card()))
            }
        })
    }
}