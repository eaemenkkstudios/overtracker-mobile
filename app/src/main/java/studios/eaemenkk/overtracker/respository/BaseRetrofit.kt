package studios.eaemenkk.overtracker.respository

import android.content.Context
import android.webkit.HttpAuthHandler
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import studios.eaemenkk.overtracker.R
import java.util.concurrent.TimeUnit

open class BaseRetrofit(context: Context, baseUrl: String) {
    val retrofit: Retrofit
    private val gson: Gson

    init {
        val logInterceptor = HttpLoggingInterceptor()
        logInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val cookieInterceptor = CookieInterceptor(context)

        val client = OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(logInterceptor)
            .addInterceptor(cookieInterceptor)
            .build()

        gson = GsonBuilder().create()

        retrofit = Retrofit.Builder()
            .client(client)
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

}

class CookieInterceptor(val context: Context): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val sharedPreferences = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
        val session = sharedPreferences.getString("session", null)
        if(session.isNullOrEmpty()) return chain.proceed(request)
        request = request
            .newBuilder()
            .addHeader("Cookie", "connect.sid=$session; Path=/; HttpOnly; Domain=${context.getString(R.string.api_base_url)}")
            .build()
        return chain.proceed(request)
    }

}