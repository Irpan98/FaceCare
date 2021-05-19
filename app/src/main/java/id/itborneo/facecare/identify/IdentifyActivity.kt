package id.itborneo.facecare.identify

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
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