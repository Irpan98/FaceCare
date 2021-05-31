package id.itborneo.facecare.profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ShareCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.firebase.auth.FirebaseAuth
import id.itborneo.facecare.core.ThisRepository
import id.itborneo.facecare.databinding.FragmentProfileBinding
import id.itborneo.facecare.home.HomeViewModel
import id.itborneo.facecare.utils.KsPrefUser
import id.itborneo.facecare.utils.enums.Status


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
        btnListener()
    }

    private fun btnListener() {
        binding.btnContactUs.setOnClickListener {
            composeEmail()
        }
        binding.btnLeaveFeedback.setOnClickListener {
            sendFeedback()
        }
    }


    private fun sendFeedback(){
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://forms.gle/adJ6wM1u9oMPQqwk7"))
        startActivity(browserIntent)
    }
    private fun updateUI() {


        ThisRepository().getSingleUserInfo(KsPrefUser.getUserId()).observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    val data = it.data
                    binding.userName.text = data?.name

                }
            }

        }

        ThisRepository().getSingleIdentifyUser(KsPrefUser.getUserId()).observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    val data = it.data
                    binding.tvSkinType.text = data?.skinType
                    binding.tvSkinGoal.text = data?.goals

                }
            }

        }
    }

    private fun logoutVisibility() {
        if (FirebaseAuth.getInstance().currentUser?.uid == null) {
            binding.tvLoginFirst.visibility = View.VISIBLE
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

    fun composeEmail() {
        ShareCompat.IntentBuilder.from(requireActivity())
            .setType("message/rfc822")
            .addEmailTo("rokutech@gmail.com")
//            .setSubject("FaceCare Feedback")
//            .setText(body)
            //.setHtmlText(body) //If you are using HTML in your body text
            .setChooserTitle("Contact Rokutech")
            .startChooser();
    }
}