package id.itborneo.facecare.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.preference.PreferenceManager
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import id.itborneo.facecare.R
import id.itborneo.facecare.main.MainActivity
import id.itborneo.facecare.onboarding.AppIntroActivity
import id.itborneo.facecare.utils.PREF_FIRST_TIME


class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        initUI()

        initTimer()
    }

    @Suppress("DEPRECATION")
    private fun initUI() {

        val UI_OPTIONS: Int =
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        window.decorView.systemUiVisibility = UI_OPTIONS
    }

    @Suppress("DEPRECATION")
    private fun initTimer() {
        val handler = Handler()
        handler.postDelayed({

            if (!notFirstTime()) { //firestime
                moveToIntroApp()
            } else {
                moveToLogin()
            }

        }, 2000)
    }

    private fun notFirstTime(): Boolean {
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(
            baseContext
        )

        return sharedPref.getBoolean(PREF_FIRST_TIME, false)

    }

    private fun moveToIntroApp() {
        val intent = Intent(this, AppIntroActivity::class.java)
        startActivity(intent)
    }

    private fun moveToLogin() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
//        val intent = Intent(this, MainActivity::class.java)
//        startActivity(intent)
    }
}