package id.itborneo.facecare.identify

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatRadioButton
import com.google.firebase.database.FirebaseDatabase
import id.itborneo.facecare.databinding.ActivityIdentifyBinding
import id.itborneo.facecare.model.IdentifyModel
import id.itborneo.facecare.utils.KsPrefUser


class IdentifyActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "IdentifyActivity"

        fun getInstance(context: Context) {
            val intent = Intent(context, IdentifyActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            context.startActivity(intent)
        }
    }

    private lateinit var binding: ActivityIdentifyBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
        buttonListener()
        initFaceProblem()
    }

    private fun initFaceProblem() {
        //Defining 5 buttons.
        //Defining 5 buttons.
        val buttons = 5
        val rb = arrayOfNulls<AppCompatRadioButton>(buttons)

        val rgp = binding.rgFaceProblem
        rgp.orientation = LinearLayout.VERTICAL

        for (i in 1..buttons) {
            val rbn = RadioButton(this)
            rbn.id = View.generateViewId()
            rbn.text = "RadioButton$i"
            val params =
                LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1f
                )
            rbn.layoutParams = params
            rgp.addView(rbn)
        }

    }

    private fun buttonListener() {


        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("users")



        binding.btnSubmit.setOnClickListener {
            val gender = binding.etIdentifyGender.text.toString()
            val faceProbem = binding.etIdentifyFaceProblem.text.toString()
            val skinType = binding.etIdentifySkinType.text.toString()


            val identify = IdentifyModel(
                gender,
                faceProbem,
                skinType
            )

            val userId = KsPrefUser.getUser()
            myRef.child(userId).setValue(identify)
                .addOnSuccessListener {
                    Log.d(TAG, "success add data : $it")

                }
                .addOnFailureListener {
                    Log.e(TAG, "error add data ${it.message}")

                }

        }
    }

    private fun initBinding() {
        binding = ActivityIdentifyBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }


}