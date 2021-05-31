package id.itborneo.facecare.analyzing

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import id.itborneo.facecare.R
import id.itborneo.facecare.core.local.AppDatabase
import id.itborneo.facecare.core.ml.Classifier
import id.itborneo.facecare.core.model.RecognitionModel
import id.itborneo.facecare.core.model.ResultModel
import id.itborneo.facecare.databinding.ActivityAnalyzingBinding
import id.itborneo.facecare.result.ResultActivity
import kotlinx.coroutines.*


class AnalyzingActivity : AppCompatActivity() {

    var results = MutableLiveData<ArrayList<RecognitionModel>>()

    companion object {
        private const val TAG = "AnalyzingActivity"
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
    private val dataImage = MutableLiveData<Bitmap>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
        retrieveData()
        buttonListener()

        observeImage()
        observeResult()
    }

    private fun observeResult() {

    }

    private fun buttonListener() {
        binding.btnToResult.setOnClickListener {
            actionToResult()
        }
    }

    private fun updateUI(bitmap: Bitmap) {

        binding.ivImage.setImageBitmap(bitmap)
    }

    private fun initBinding() {
        binding = ActivityAnalyzingBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

    }

    private fun observeImage() {

        dataImage.observe(this) {
            anayzeWithTensorFlow(it)
            updateUI(it)
        }


    }


    private fun retrieveData() {

        getUri = intent.extras?.getParcelable(EXTRA_ANALYZING)
        Log.d(TAG, "retrieveData $getUri")
        val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, getUri)
        dataImage.value = bitmap

    }

    private fun actionToResult() {
        ResultActivity.getInstance(this, results.value)
    }


    private fun anayzeWithTensorFlow(bitmap: Bitmap) {


//        val mModelPath = "model_acnes_vgg16_2.tflite"
        val mModelPath = "acnes_own_model_2.tflite"
        val mLabelPath = "label.txt"
        val mInputSize = 224
        val classifier = Classifier(assets, mModelPath, mLabelPath, mInputSize)

        val result2 = classifier.recognizeImage(bitmap)

        val arrayListResult = ArrayList<RecognitionModel>()
        result2.forEach {
            val data = RecognitionModel(
                it.id,
                it.title,
                it.confidence
            )
            arrayListResult.add(data)
        }

        results.value = arrayListResult

        var text = ""

        result2.forEachIndexed { index, item ->
            text += " $index : ${item.title} = ${item.confidence} "

        }

        updateViewResult(text)
    }

    private fun updateViewResult(text: String) {
        val textView1: TextView = findViewById(R.id.textView1)
        textView1.text = text
    }


}