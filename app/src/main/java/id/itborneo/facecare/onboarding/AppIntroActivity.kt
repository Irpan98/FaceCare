package id.itborneo.facecare.onboarding

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.fragment.app.Fragment
import com.github.appintro.AppIntro
import id.itborneo.facecare.R
import id.itborneo.facecare.main.MainActivity
import id.itborneo.facecare.utils.PREF_FIRST_TIME


class AppIntroActivity : AppIntro() {

    //referensi
    //https://github.com/AppIntro/AppIntro
    @SuppressLint("MissingSuperCall")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupAppIntro()
        initOnBoarding()
    }

    private fun setupAppIntro() {
        showStatusBar(false)

        isWizardMode = true
        setImmersiveMode()
        isSystemBackButtonLocked = true


//        setTransformer(AppIntroPageTransformerType.SlideOver)


    }

    private fun initOnBoarding() {
        addSlide(
            OnBoardingContentFragment.newInstance(
                R.drawable.ic_intro_1,
                "Day-to-day Progress",
                "Assess your progress with daily entries."
            )

        )
        addSlide(

            OnBoardingContentFragment.newInstance(
                R.drawable.ic_intro_2,
                "Photograph your milestone",
                "You can see your timestamp albums and see your progress over time from beginning to end"
            )
        )
        addSlide(
            OnBoardingContentFragment.newInstance(
                R.drawable.ic_intro_3,
                "Share your story here",
                "Face care is 80+ million women willing to support each other and  and share their story"
            )
        )

    }

    override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)
        safePref()
        moveActivity()
    }

    override fun onSkipPressed(currentFragment: Fragment?) {
        super.onSkipPressed(currentFragment)
        safePref()
        moveActivity()
    }

    private fun moveActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)

//        val intent = Intent(this, MainActivity::class.java)
//        startActivity(intent)
    }


    private fun safePref() {
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(baseContext)

        val editor = sharedPref.edit()
        editor.putBoolean(PREF_FIRST_TIME, true)
        editor.apply()
    }

}