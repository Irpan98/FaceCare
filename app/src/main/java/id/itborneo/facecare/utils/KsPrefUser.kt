package id.itborneo.facecare.utils

import android.content.Context
import com.cioccarellia.ksprefs.KsPrefs

object KsPrefUser {

    private lateinit var appContext: Context
    private val prefs by lazy { KsPrefs(appContext) }

    val NOT_REGISTERED = "not_registered "
    fun instance(context: Context) {
        appContext = context
    }


    private const val USER_KEY = "user_key"
    fun setUserId(idUser: String) {

        prefs.push(USER_KEY, idUser)
        prefs.save()

    }

    fun getUserId() = prefs.pull(USER_KEY, NOT_REGISTERED)

    fun removeUserId() = prefs.remove(USER_KEY)

}