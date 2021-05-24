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
                R.drawable.ic_illustration_login,
                "Dengar",
                "Dengarkan intisari dari \n berbagai buku pengembangan diri"
            )

        )
        addSlide(

            OnBoardingContentFragment.newInstance(
                R.drawable.ic_illustration_register,
                "Belajar",
                "Belajar dari mana saja  \n dan kapan saja"
            )
        )
        addSlide(
            OnBoardingContentFragment.newInstance(
                R.drawable.ic_illusrtation_not_register,
                "Pintar",
                "Tingkatkan keterampilan, \n On The Go"
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