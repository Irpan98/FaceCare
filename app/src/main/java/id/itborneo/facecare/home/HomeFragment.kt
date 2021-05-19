package id.itborneo.facecare.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
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
        view.findViewById<Button>(R.id.btn_home_login).setOnClickListener {
            LoginActivity.getInstance(requireContext())
        }
        view.findViewById<Button>(R.id.btn_home_identify).setOnClickListener {
            IdentifyActivity.getInstance(requireContext())
        }

        view.findViewById<Button>(R.id.btn_home_register).setOnClickListener {
            RegisterActivity.getInstance(requireContext())
        }
    }

    private fun homeStateObserver() {
        viewModel.homeState.observe(viewLifecycleOwner) {
            when (it) {
                HomeEnum.NOT_REGISTERED -> {
                    binding.tvUserStatus.text = "NOT_REGISTERED"
                }
                HomeEnum.REGISTERED -> {
                    binding.tvUserStatus.text = "REGISTERED"
                    observeUserData()
                }
                HomeEnum.IDENTIFIED -> {
                    binding.tvUserStatus.text = "IDENTIFIED"
                    observeUserData()
                }
            }
        }

    }

    private fun loginChecker() {
        val user = KsPrefUser.getUser()
        if (user == KsPrefUser.NOT_REGISTERED) {
            viewModel.homeState.value = HomeEnum.NOT_REGISTERED

        } else {
            viewModel.idUser = user
            viewModel.homeState.value = HomeEnum.REGISTERED
        }

    }

    override fun onResume() {
        super.onResume()
        loginChecker()

    }

    private var userDataObserved = false
    private fun observeUserData() {
        if (userDataObserved) return
        userDataObserved = true
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("users").child(viewModel.idUser)


        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val map = dataSnapshot.value as Map<String, Any>?

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


}