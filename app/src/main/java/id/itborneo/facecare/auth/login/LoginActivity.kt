package id.itborneo.facecare.auth.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import id.itborneo.facecare.auth.register.RegisterActivity
import id.itborneo.facecare.databinding.ActivityLoginBinding
import id.itborneo.facecare.utils.KsPrefUser
import id.itborneo.facecare.utils.extension.hideKeyboard
import id.itborneo.facecare.utils.validation.NullChecker

class LoginActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "ResultActivity"

        fun getInstance(context: Context) {
            val intent = Intent(context, LoginActivity::class.java)
//            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            context.startActivity(intent)
        }

        fun getInstance(context: Context, launcher: ActivityResultLauncher<Intent>) {
            val intent = Intent(context, LoginActivity::class.java)
//            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)

            launcher.launch(intent)
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
            if (!isInputValid()) return@setOnClickListener

            val email = binding.etLoginEmail.text.toString()
            val password = binding.etLoginPassword.text.toString()
            Log.d(TAG, "login click $email dan $password")
            submitLogin(email, password)
        }

        binding.btnLoginRegister.setOnClickListener {
            finish()
            RegisterActivity.getInstance(this)
        }
    }

    private fun submitLogin(email: String, password: String) {

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                this.hideKeyboard()
                Log.d(TAG, "login berhasil $it")
                loginSuccess()
            }
            .addOnFailureListener {
                this.hideKeyboard()
                Toast.makeText(this, "Email / Password is Wrong's ", Toast.LENGTH_SHORT).show()
                Log.e(TAG, "error create user ${it.message} $")

            }
    }

    private fun loginSuccess() {
        val userUid = auth.currentUser?.uid
        Log.d(TAG, "loginSuccess $userUid")

        if (userUid != null) {
            KsPrefUser.setUserId(userUid)
            Toast.makeText(this, "Welcome", Toast.LENGTH_LONG).show()
            setResult(RESULT_OK)
            finish()
        }
    }


    private fun initBinding() {
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    private fun isInputValid(): Boolean {

        val isEmailValid =
            NullChecker(this).isInputValid(binding.etLoginEmail, "Email tidak boleh kosong")

        if (!isEmailValid) return false


        val ispasswordValid =
            NullChecker(this).isInputValid(binding.etLoginPassword, "Password tidak boleh kosong")
        if (!ispasswordValid) return false


        return !(!isEmailValid || !ispasswordValid)
    }

}