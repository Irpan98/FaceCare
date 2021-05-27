package id.itborneo.facecare.analyzing

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import id.itborneo.facecare.databinding.ActivityAnalyzingBinding
import id.itborneo.facecare.result.ResultActivity


class AnalyzingActivity : AppCompatActivity() {


    companion object {
        private const val TAG = "ResultActivity"
        private const val EXTRA_ANALYZING = "extra_analyzing"

        fun getInstance(context: Context, savedUri: Uri) {
            val intent = Intent(context, AnalyzingActivity::class.java)
//            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            intent.putExtra(EXTRA_ANALYZING, savedUri)

            context.startActivity(intent)
        }
    }

    var getUri: Uri? = null
    private lateinit var binding: ActivityAnalyzingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
        retrieveData()
        updateUI()
        buttonListener()
    }

    private fun buttonListener() {
        binding.btnToResult.setOnClickListener {
            actionToResult()
        }
    }

    private fun updateUI() {
        val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, getUri)
        binding.ivImage.setImageBitmap(bitmap)
    }

    private fun initBinding() {
        binding = ActivityAnalyzingBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    private fun retrieveData() {
        getUri = intent.extras?.getParcelable(EXTRA_ANALYZING)
        Log.d(TAG, "retrieveData $getUri")
    }

    private fun actionToResult() {
        ResultActivity.getInstance(this)
    }

}