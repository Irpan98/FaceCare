package id.itborneo.facecare.community

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import id.itborneo.facecare.R
import id.itborneo.facecare.core.ThisRepository
import id.itborneo.facecare.utils.enums.Status

class CommunityActivity : AppCompatActivity() {


    companion object {
        private const val TAG = "ResultActivity"

        fun getInstance(context: Context) {
            val intent = Intent(context, CommunityActivity::class.java)
//            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            context.startActivity(intent)
        }
    }

    private val TAG = "CommunityActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_community)

        ThisRepository().getListChat().observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    val data = it.data
                    Log.d(TAG, data.toString())
                }
            }
        }
    }
}