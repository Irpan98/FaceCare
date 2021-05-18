package id.itborneo.facecare.auth.register

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import id.itborneo.facecare.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "ResultActivity"

        fun getInstance(context: Context) {
            val intent = Intent(context, RegisterActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            context.startActivity(intent)
        }
    }

    private lateinit var binding: ActivityRegisterBinding

    val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
        buttonListener()
    }

    private fun initBinding() {
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    private fun buttonListener() {

        binding.btnRegisterRegister.setOnClickListener {
            val email = binding.etRegisterEmail.text.toString()
            val password = binding.etRegisterPassword.text.toString()
            Log.d(TAG, "register click $email dan $password")

            submitLogin(email, password)
        }

        binding.btnRegisterLogin.setOnClickListener {
            finish()
        }
    }

    private fun submitLogin(email: String, password: String) {


        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                Log.d(TAG, "register berhasil $it")
                getUid()
            }
            .addOnFailureListener {
                Log.e(TAG, "register failed ${it.message}")

            }


    }

    private fun getUid() {
        val userUid = auth.currentUser?.uid
        Log.d(TAG, "getUid $userUid")

    }
}