package id.itborneo.facecare.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.firebase.auth.FirebaseAuth
import id.itborneo.facecare.databinding.FragmentProfileBinding
import id.itborneo.facecare.home.HomeViewModel
import id.itborneo.facecare.utils.KsPrefUser


class ProfileFragment : Fragment() {


    private val viewModel: HomeViewModel by viewModels()
    private lateinit var binding: FragmentProfileBinding

    private val TAG = "ProfileFragment"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateUI()
        logoutListener()
        logoutVisibility()
    }

    private fun updateUI() {

    }

    private fun logoutVisibility() {
        if (FirebaseAuth.getInstance().currentUser?.uid == null) {
//            binding.tvLoginFirst.visibility = View.VISIBLE
        }
    }

    private fun logoutListener() {

        binding.btnLogOut.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            KsPrefUser.removeUserId()
            requireActivity().apply {
                finish()
                startActivity(intent)
            }
        }


    }
}