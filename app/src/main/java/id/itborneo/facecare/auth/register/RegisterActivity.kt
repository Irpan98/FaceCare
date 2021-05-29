package id.itborneo.facecare.auth.register

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import id.itborneo.facecare.R
import id.itborneo.facecare.auth.login.LoginActivity
import id.itborneo.facecare.core.model.UserInfoModel
import id.itborneo.facecare.databinding.ActivityRegisterBinding
import id.itborneo.facecare.utils.extension.hideKeyboard
import id.itborneo.facecare.utils.ui.ItBorneoToast
import id.itborneo.facecare.utils.validation.NullChecker

class RegisterActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "RegisterActivity"

        fun getInstance(context: Context) {
            val intent = Intent(context, RegisterActivity::class.java)
//            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
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
            if (!isInputValid()) return@setOnClickListener


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
                this.hideKeyboard()
                ItBorneoToast.toastMain(this, "Register Success, please Login")
                setResult(RESULT_OK)
                finish()

                Log.d(TAG, "success add data : $it")

            }
            .addOnFailureListener {
                this.hideKeyboard()
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
                Log.e(TAG, "${it.message}")
                when (it.message) {

                    "The email address is badly formatted."
                    -> {
                        Toast.makeText(this, "Please input valid email address", Toast.LENGTH_SHORT)
                            .show()
                    }
                    "The given password is invalid. [ Password should be at least 6 characters ]"
                    -> {
                        Toast.makeText(
                            this,
                            "Please input password at least 6 characters",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    "The email address is already in use by another account."
                    -> {
                        Toast.makeText(
                            this,
                            "Email already registered",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
    }

    private fun getUid(): String? {
        val userUid = auth.currentUser?.uid
        Log.d(TAG, "getUid $userUid")
        return userUid
    }

    private fun isInputValid(): Boolean {

        val isFullnameValid =
            NullChecker(this).isInputValid(
                binding.etRegisterName,
                getString(R.string.no_empty_fullname)
            )

        if (!isFullnameValid) return false

        val isEmailValid =
            NullChecker(this).isInputValid(
                binding.etRegisterEmail,
                getString(R.string.no_empty_email)
            )

        if (!isEmailValid) return false

        val ispasswordValid =
            NullChecker(this).isInputValid(
                binding.etRegisterPassword,
                getString(R.string.no_empty_password)
            )
        if (!ispasswordValid) return false


        return !(!isEmailValid || !ispasswordValid || !isFullnameValid)
    }
}