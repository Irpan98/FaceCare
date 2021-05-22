package id.itborneo.facecare.auth.register

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import id.itborneo.facecare.auth.login.LoginActivity
import id.itborneo.facecare.databinding.ActivityRegisterBinding
import id.itborneo.facecare.core.model.UserInfoModel

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

            submitRegister(email, password)

        }

        binding.btnRegisterLogin.setOnClickListener {
            finish()
            LoginActivity.getInstance(this)

        }


    }

    private fun submitUserdata() {

        val userId = getUid() ?: return


        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("users")

        val getInputName = binding.etRegisterName.text.toString()

        val identify = UserInfoModel(
            name = getInputName
        )

        myRef.child(userId).setValue(identify)
            .addOnSuccessListener {
                Toast.makeText(this, "Register Success, please Login", Toast.LENGTH_LONG)
                setResult(RESULT_OK)
                finish()

                Log.d(TAG, "success add data : $it")

            }
            .addOnFailureListener {
                Log.e(TAG, "error add data ${it.message}")

            }


    }


    private fun submitRegister(email: String, password: String) {


        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                Log.d(TAG, "register berhasil $it")
                getUid()
                submitUserdata()
            }
            .addOnFailureListener {
                Log.e(TAG, "register failed ${it.message}")

            }


    }

    private fun getUid(): String? {
        val userUid = auth.currentUser?.uid
        Log.d(TAG, "getUid $userUid")
        return userUid
    }
}