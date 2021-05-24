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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import id.itborneo.facecare.R
import id.itborneo.facecare.article.ArticleActivity
import id.itborneo.facecare.article.ArticleAdapter
import id.itborneo.facecare.auth.login.LoginActivity
import id.itborneo.facecare.auth.register.RegisterActivity
import id.itborneo.facecare.core.model.UserInfoModel
import id.itborneo.facecare.core.networks.FaceCaseFirebase
import id.itborneo.facecare.databinding.FragmentHomeBinding
import id.itborneo.facecare.identify.IdentifyActivity
import id.itborneo.facecare.utils.KsPrefUser
import id.itborneo.facecare.utils.enums.HomeEnum
import id.itborneo.facecare.utils.enums.Status
import id.itborneo.ugithub.core.factory.ViewModelFactory


class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels {
        val userId = KsPrefUser.getUserId()
        ViewModelFactory(userId)
    }

    private val binding: FragmentHomeBinding by viewBinding(FragmentHomeBinding::bind)

    private val TAG = "HomeFragment"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buttonListener(view)
        homeStateObserver()

        loginChecker()
        observeIdentifyUserData()

        initArticleNews()

    }

    private fun buttonListener(view: View) {

    }

    private fun homeStateObserver() {
        viewModel.homeState.observe(viewLifecycleOwner) {
            viewStateVisibility(it)
            when (it) {
                HomeEnum.NOT_REGISTERED -> {
                    viewNotRegistered()
                }
                HomeEnum.REGISTERED -> {
                    viewRegistered()
                    viewModel.setSingleIdentifyUser()
                }
                HomeEnum.IDENTIFIED -> {
                    viewModel.setSingleIdentifyUser()

                    viewIdentified()
                }
            }
        }

    }

    private fun viewIdentified() {
//        binding.tvUserStatus.text = "IDENTIFIED, Hi"
        binding.incHomeIdentified.apply {
            btnHomeReidentify.setOnClickListener {
                actionToIdentify()
            }

            tvHomeSkinType.text = viewModel.userIdentifiedModel.value?.skinType


        }


    }

    private fun initArticleNews() {

        binding.incHomeIdentified.btnViewAllNews.setOnClickListener {
            ArticleActivity.getInstance(requireContext())
        }

        binding.incHomeIdentified.rvArticle.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)

        val articleAdapter = ArticleAdapter() {

        }
        binding.incHomeIdentified.rvArticle.adapter = articleAdapter

        viewModel.getArticle().observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    val data = it.data
                    if (data != null) {
                        articleAdapter.set(data)
                    }
                }
            }
        }
    }


    private fun actionToIdentify() {
        IdentifyActivity.getInstance(requireContext())

    }

    private val firebase = FaceCaseFirebase

    private fun viewRegistered() {
        binding.incHomeRegistered.apply {
            btnHomeIdentify.setOnClickListener {
                actionToIdentify()
            }

            val user = firebase.getUser(KsPrefUser.getUserId())

            user.addValueEventListener(object : ValueEventListener {
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

    private fun viewNotRegistered() {

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
        val user = KsPrefUser.getUserId()

        Log.d(TAG, "loginChecker() $user")
        if (user == KsPrefUser.NOT_REGISTERED) {
            viewModel.homeState.value = HomeEnum.NOT_REGISTERED

        } else {
//            viewModel.idUser = user
            viewModel.homeState.value = HomeEnum.REGISTERED

        }

    }

    private fun observeIdentifyUserData() {

        viewModel.getIdentifyUser().observe(viewLifecycleOwner) {
            viewModel.userIdentifiedModel.value = it.data
            Log.d(TAG, "observeIdentifyUserData ${it.data}")

            if (it.data != null) {
                Log.d(TAG, "observeIdentifyUserData ${it.data}")
                viewModel.homeState.value = HomeEnum.IDENTIFIED
            }
        }
    }


    private fun viewStateVisibility(state: HomeEnum) {

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