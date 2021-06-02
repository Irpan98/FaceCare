package id.itborneo.facecare.analyzing

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Matrix
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import id.itborneo.facecare.R
import id.itborneo.facecare.core.ml.Classifier
import id.itborneo.facecare.core.model.RecognitionModel
import id.itborneo.facecare.databinding.ActivityAnalyzingBinding
import id.itborneo.facecare.result.ResultActivity


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

    private lateinit var cropActivityResultLauncher: ActivityResultLauncher<Any?>

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

        cropImageInitialize()

    }

    private fun cropImageInitialize() {
//        cropActivityResultLauncher = registerForActivityResult(cropActivityResultContract) {
//            it?.let { getUri }
//        }

    }

//    private val cropActivityResultContract = object : ActivityResultContract<Any?, Uri?>() {
//        override fun createIntent(context: Context, input: Any?): Intent {
//            return CropImage.activity()
//                .setAspectRatio(4, 3)
//                .getIntent(this@AnalyzingActivity)
//        }
//
//        override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
//            return CropImage.getActivityResult(intent)?.uri
//
//        }
//
//    }

    private fun observeResult() {

    }

    private fun buttonListener() {
        binding.btnToResult.setOnClickListener {
            actionToResult()
        }
        binding.btnToCrop.setOnClickListener {
//            cropActivityResultLauncher.launch(null)
            binding.clParentCropImage.visibility = View.VISIBLE

        }
        binding.btnCropThisImage.setOnClickListener {
            dataImage.value = binding.cropImageView.croppedImage
            binding.clParentCropImage.visibility = View.GONE

        }
    }

    fun Bitmap.rotate(degrees: Float): Bitmap {
        val matrix = Matrix().apply { postRotate(degrees) }
        return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
    }

    private fun updateUI(bitmap: Bitmap) {


        binding.ivImage.setImageBitmap(bitmap)
//        Glide.with(this)
//            .load(bitmap)
//            .placeholder(R.drawable.ic_image_placeholder)
//            .into(binding.ivImage)
    }

    private fun initBinding() {
        binding = ActivityAnalyzingBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

    }

    private fun observeImage() {

        dataImage.observe(this) {
            binding.cropImageView.setImageBitmap(it)
            anayzeWithTensorFlow(it)
            updateUI(it)
        }

    }


    private fun retrieveData() {

        getUri = intent.extras?.getParcelable(EXTRA_ANALYZING)
        Log.d(TAG, "retrieveData $getUri")
        val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, getUri)
        dataImage.value = bitmap.rotate(-90F)

    }

    private fun actionToResult() {
        val result = results.value
        if (result != null) {
            val validResult = GET_VALID_RESULT(result)
            ResultActivity.getInstance(this, validResult)

        }
    }


    private fun anayzeWithTensorFlow(bitmap: Bitmap) {


//        val mModelPath = "model_acneDs_vgg16_2.tflite"
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

    private fun GET_VALID_RESULT(list: ArrayList<RecognitionModel>): ArrayList<RecognitionModel> {
        val valid = 0.6


        val result = ArrayList<RecognitionModel>()

        list.forEach {
            if (it.confidence >= valid) {
                result.add(it)
            }
        }

        return result

    }


    fun rotateImage(source: Bitmap, angle: Float): Bitmap? {
        val matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(
            source, 0, 0, source.width, source.height,
            matrix, true
        )
    }


}