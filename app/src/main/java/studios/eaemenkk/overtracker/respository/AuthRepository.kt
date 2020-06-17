package studios.eaemenkk.overtracker.respository

import android.content.Context
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.GET
import studios.eaemenkk.overtracker.R
import studios.eaemenkk.overtracker.domain.RequestResult

interface AuthService {
    @GET("/isauth")
    fun isAuth(): Call<Void>
}

class AuthRepository(val context: Context, baseUrl: String) : BaseRetrofit(context, baseUrl) {
    private val service = retrofit.create(AuthService::class.java)

    fun login(session: String, callback: (authResult: RequestResult) -> Unit) {
        val sharedPreferences = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE).edit()
        sharedPreferences.putString("session", session)
        sharedPreferences.apply()
        callback(RequestResult(true, ""))
    }

    fun logout(callback: (authResult: RequestResult) -> Unit) {
        val sharedPreferences = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE).edit()
        sharedPreferences.remove("session")
        sharedPreferences.apply()
        callback(RequestResult(true, context.getString(R.string.logged_out)))
    }

    fun isAuth(callback: (authResult: RequestResult) -> Unit) {
        service.isAuth().enqueue(object: Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.code() == 200) {
                    callback(RequestResult(true, context.getString(R.string.login_success)))
                } else {
                    callback(RequestResult(false, context.getString(R.string.login_failed)))
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                callback(RequestResult(false, context.getString(R.string.login_failed)))
            }
        })
    }

}
