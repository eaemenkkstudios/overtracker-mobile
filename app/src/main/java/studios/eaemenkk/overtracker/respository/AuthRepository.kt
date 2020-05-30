package studios.eaemenkk.overtracker.respository

import android.content.Context
import studios.eaemenkk.overtracker.R
import studios.eaemenkk.overtracker.domain.RequestResult


class AuthRepository(val context: Context, baseUrl: String) : BaseRetrofit(context, baseUrl) {

    fun login(session: String, callback: (authResult: RequestResult) -> Unit) {
        val sharedPreferences = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE).edit()
        sharedPreferences.putString("session", session)
        sharedPreferences.apply()
        callback(RequestResult(true, context.getString(R.string.login_success)))
    }

    fun logout(callback: (authResult: RequestResult) -> Unit) {
        val sharedPreferences = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE).edit()
        sharedPreferences.remove("session")
        sharedPreferences.apply()
        callback(RequestResult(true, context.getString(R.string.logged_out)))
    }

}
