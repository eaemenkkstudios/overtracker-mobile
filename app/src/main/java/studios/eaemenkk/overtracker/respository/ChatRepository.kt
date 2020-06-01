package studios.eaemenkk.overtracker.respository

import android.content.Context
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import studios.eaemenkk.overtracker.domain.Message
import studios.eaemenkk.overtracker.domain.MessageToServer

interface ChatService {
    @POST("/dialogflow")
    fun sendMessageToBot(
        @Body message: MessageToServer
    ): Call<Message>
}

class ChatRepository(context: Context, baseUrl: String) : BaseRetrofit(context, baseUrl) {
    private val service = retrofit.create(ChatService::class.java)

    fun sendMessageToBot(message: String, callback: (message: Message?) -> Unit) {
        service.sendMessageToBot(MessageToServer(message)).enqueue(object: Callback<Message?> {
            override fun onResponse(call: Call<Message?>, response: Response<Message?>) {
                callback(response.body())
            }

            override fun onFailure(call: Call<Message?>, t: Throwable) {
                callback(null)
            }
        })
    }
}