package id.itborneo.facecare.home

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import id.itborneo.facecare.article.views.ArticleActivity
import id.itborneo.facecare.article.ArticleAdapter
import id.itborneo.facecare.auth.login.LoginActivity
import id.itborneo.facecare.auth.register.RegisterActivity
import id.itborneo.facecare.core.factory.ViewModelFactory
import id.itborneo.facecare.databinding.FragmentHomeBinding
import id.itborneo.facecare.identify.IdentifyActivity
import id.itborneo.facecare.utils.KsPrefUser
import id.itborneo.facecare.utils.enums.HomeEnum
import id.itborneo.facecare.utils.enums.Status


class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels {
        val userId = KsPrefUser.getUserId()
        ViewModelFactory(userId)
    }

    private lateinit var binding: FragmentHomeBinding

    private val TAG = "HomeFragment"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeStateObserver()
        loginChecker()

        initArticleNews()
        observeIdentify()
        observeIUserInfo()
        initButtonListener()
    }

    private fun initButtonListener() {
        binding.incHomeIdentified.btnHomeIdentify.setOnClickListener {
            actionToIdentify()
        }
    }

    private fun observeIUserInfo() {
        viewModel.getUserInfo().observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    val data = it.data
                    val welcomeUser = "Welcome \n ${data?.name}"
                    binding.incHomeIdentified.tvName.text = welcomeUser
                }
            }

        }
    }


    private fun viewIdentified() {
//        binding.tvUserStatus.text = "IDENTIFIED, Hi"
        binding.incHomeIdentified.apply {
            btnHomeIdentify.text = "Re-Identify"
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


    private fun viewRegistered() {

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

        viewModel.refreshUserId(user)
        if (user == KsPrefUser.NOT_REGISTERED) {
            viewModel.homeState.value = HomeEnum.NOT_REGISTERED
        } else {
            viewModel.homeState.value = HomeEnum.REGISTERED
        }

    }

    private fun observeIdentify() {

        viewModel.getIdentifyUser().observe(viewLifecycleOwner) {
            viewModel.userIdentifiedModel.value = it.data

            if (it.data != null) {
                viewModel.homeState.value = HomeEnum.IDENTIFIED
            }
        }
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
//                    viewModel.setSingleIdentifyUser()
                }
                HomeEnum.IDENTIFIED -> {
//                    viewModel.setSingleIdentifyUser()

                    viewIdentified()
                }
            }
        }

    }

    private fun viewStateVisibility(state: HomeEnum) {

        val viewState =
            listOf(binding.incHomeNotRegister)

        viewState.forEach {
            it.root.visibility = View.GONE
        }

        when (state) {
            HomeEnum.NOT_REGISTERED -> binding.incHomeNotRegister.root.visibility = View.VISIBLE
//            HomeEnum.REGISTERED -> binding.incHomeRegistered.root.visibility = View.VISIBLE
//            HomeEnum.IDENTIFIED -> binding.incHomeIdentified.root.visibility = View.VISIBLE
        }

    }

    val getContent =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                loginChecker()
            }
        }


}