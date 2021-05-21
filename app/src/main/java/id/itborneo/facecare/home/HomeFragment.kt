package id.itborneo.facecare.home

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import id.itborneo.facecare.R
import id.itborneo.facecare.auth.login.LoginActivity
import id.itborneo.facecare.auth.register.RegisterActivity
import id.itborneo.facecare.databinding.FragmentHomeBinding
import id.itborneo.facecare.identify.IdentifyActivity
import id.itborneo.facecare.model.UserIdentifiedModel
import id.itborneo.facecare.model.UserInfoModel
import id.itborneo.facecare.utils.KsPrefUser
import id.itborneo.facecare.utils.enums.HomeEnum


class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels()
    private val binding: FragmentHomeBinding by viewBinding(FragmentHomeBinding::bind)

    private val TAG = "HomeFragment"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buttonListener(view)
        homeStateObserver()

        loginChecker()
    }

    private fun buttonListener(view: View) {

    }

    private fun homeStateObserver() {
        viewModel.homeState.observe(viewLifecycleOwner) {
            viewStateVisibility(it)
            when (it) {
                HomeEnum.NOT_REGISTERED -> {
                    notRegisteredView()
                }
                HomeEnum.REGISTERED -> {
                    registeredView()
                    observeUserData()
                }
                HomeEnum.IDENTIFIED -> {
                    identifiedView()
                    observeUserData()
                }
            }
        }

    }

    private fun identifiedView() {
//        binding.tvUserStatus.text = "IDENTIFIED, Hi"
        binding.incHomeIdentified.apply {
            btnHomeReidentify.setOnClickListener {
                actionToIdentify()
            }

            tvHomeSkinType.text = viewModel.userIdentifiedModel.value?.skinType
        }


    }


    private fun actionToIdentify() {
        IdentifyActivity.getInstance(requireContext())

    }

    private fun registeredView() {
        binding.incHomeRegistered.apply {
            btnHomeIdentify.setOnClickListener {
                actionToIdentify()
            }

            val database = FirebaseDatabase.getInstance()
            val myRef = database.getReference("users").child(viewModel.idUser)


            myRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val map = dataSnapshot.value as Map<String, Any>?
                    Log.d(TAG, "registeredView Value is: $map")


                    val data = dataSnapshot.getValue(UserInfoModel::class.java)
                    viewModel.user.value = data


                    val welcomeUser = "Welcome \n ${data?.name}"
                    tvName.text = welcomeUser

                }

                override fun onCancelled(error: DatabaseError) {
                    // Failed to read value
                    Log.w(TAG, "Failed to read value.", error.toException())
                }
            })

        }
    }

    private fun notRegisteredView() {

        binding.incHomeNotRegister.apply {
            btnHomeLogin.setOnClickListener {
                LoginActivity.getInstance(requireContext(), getContent)
            }
            btnHomeRegister.setOnClickListener {
                RegisterActivity.getInstance(requireContext())
            }
        }
    }

    private fun loginChecker() {
        val user = KsPrefUser.getUser()

        Log.d(TAG, "loginChecker() $user")
        if (user == KsPrefUser.NOT_REGISTERED) {
            viewModel.homeState.value = HomeEnum.NOT_REGISTERED

        } else {
            viewModel.idUser = user
            viewModel.homeState.value = HomeEnum.REGISTERED

        }

    }

    override fun onResume() {
        super.onResume()
//        loginChecker()

    }

    private var userDataObserved = false
    private fun observeUserData() {
//        if (userDataObserved) return
//        userDataObserved = true
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("usersIdentified").child(viewModel.idUser)


        myRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val map = dataSnapshot.value as Map<String, Any>?
                val data = dataSnapshot.getValue(UserIdentifiedModel::class.java)

                viewModel.userIdentifiedModel.value = data
                Log.d(TAG, "Value is: $map")
                if (map != null) {
                    viewModel.homeState.value = HomeEnum.IDENTIFIED
                }

            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })
    }


    private fun viewStateVisibility(state: HomeEnum) {

//        val viewState =
//            listOf(binding.incHomeNotRegister, binding.incHomeRegistered, binding.incHomeIdentified)

        val viewState =
            listOf(binding.incHomeNotRegister, binding.incHomeIdentified)

        viewState.forEach {
            it.root.visibility = View.GONE
        }

        when (state) {
            HomeEnum.NOT_REGISTERED -> binding.incHomeNotRegister.root.visibility = View.VISIBLE
            HomeEnum.REGISTERED -> binding.incHomeRegistered.root.visibility = View.VISIBLE
            HomeEnum.IDENTIFIED -> binding.incHomeIdentified.root.visibility = View.VISIBLE
        }

    }


    val getContent =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                loginChecker()
            }
        }


}