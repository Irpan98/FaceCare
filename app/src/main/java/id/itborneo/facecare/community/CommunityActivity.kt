package id.itborneo.facecare.community

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import id.itborneo.facecare.R
import id.itborneo.facecare.core.ThisRepository
import id.itborneo.facecare.core.model.MessageModel
import id.itborneo.facecare.core.model.UserInfoModel
import id.itborneo.facecare.core.networks.FaceCaseFirebase
import id.itborneo.facecare.databinding.ActivityCommunityBinding
import id.itborneo.facecare.utils.KsPrefUser
import id.itborneo.facecare.utils.enums.Status
import id.itborneo.facecare.utils.extension.hideKeyboard

class CommunityActivity : AppCompatActivity() {


    companion object {
        private const val TAG = "ResultActivity"
        private const val EXTRA_USER = "user"

        fun getInstance(context: Context, userInfo: UserInfoModel?) {
            val intent = Intent(context, CommunityActivity::class.java)
//            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            intent.putExtra(EXTRA_USER, userInfo)
            context.startActivity(intent)
        }
    }

    private lateinit var binding: ActivityCommunityBinding
    private lateinit var adapter: CommunityAdapter


    private val TAG = "CommunityActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_community)
        initBinding()
        initRecycler()

        retrieveData()

        getListChat()
        initSendListener()
    }

    private fun initSendListener() {
        binding.ivSend.setOnClickListener {
            val messageText = binding.etMessage.text.toString()

            val message = MessageModel(
                messageText,
                senderId = KsPrefUser.getUserId(),
                senderName = userInfo?.name ?: ""
            )
            binding.etMessage.setText("")

            this.hideKeyboard()
            submitMessage(message)

        }
    }

    private fun submitMessage(message: MessageModel) {
        val getChat = FaceCaseFirebase.getChat()
        val key = getChat.push().key
        if (key != null) {
            getChat.child(key).setValue(message)
        }

    }

    private var userInfo: UserInfoModel? = null
    private fun retrieveData() {

        userInfo = intent.extras?.getParcelable(EXTRA_USER)

    }


    private fun getListChat() {
        ThisRepository().getListChat().observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    val data = it.data
                    Log.d(TAG, data.toString())
                    if (data != null) {
                        adapter.set(data)
                        binding.rvMessage.scrollToPosition(data.size - 1)
                    }
                }
            }
        }


    }

    private fun initBinding() {
        binding = ActivityCommunityBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    private fun initRecycler() {
        binding.rvMessage.layoutManager = LinearLayoutManager(this)
        adapter = CommunityAdapter {
//            actionToArticle(it)
        }
        binding.rvMessage.adapter = adapter
    }
}