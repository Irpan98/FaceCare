package id.itborneo.facecare.auth.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import id.itborneo.facecare.auth.register.RegisterActivity
import id.itborneo.facecare.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "ResultActivity"

        fun getInstance(context: Context) {
            val intent = Intent(context, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            context.startActivity(intent)
        }
    }

    private lateinit var binding: ActivityLoginBinding

    val auth = FirebaseAuth.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
        buttonListener()
    }

    private fun buttonListener() {

        binding.btnLoginLogin.setOnClickListener {
            val email = binding.etLoginEmail.text.toString()
            val password = binding.etLoginPassword.text.toString()
            Log.d(TAG, "login click $email dan $password")

            submitLogin(email, password)
        }

        binding.btnLoginRegister.setOnClickListener {
            RegisterActivity.getInstance(this)
        }
    }

    private fun submitLogin(email: String, password: String) {


        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                Log.d(TAG, "login berhasil $it")
                getUid()
            }
            .addOnFailureListener {
                Log.e(TAG, "error create user ${it.message}")

            }


    }

    private fun getUid() {
        val userUid = auth.currentUser?.uid
        Log.d(TAG, "getUid $userUid")

    }


    private fun initBinding() {
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }
}