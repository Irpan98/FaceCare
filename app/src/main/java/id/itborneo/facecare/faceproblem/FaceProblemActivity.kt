package id.itborneo.facecare.faceproblem

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import id.itborneo.facecare.core.model.FaceProblemModel
import id.itborneo.facecare.databinding.ActivityFaceProblemBinding

class FaceProblemActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "ProductActivity"
        private const val EXTRA_FACE_PROBLEM = "extra_face_problem"

        fun getInstance(context: Context, faceProblem: FaceProblemModel) {
            val intent = Intent(context, FaceProblemActivity::class.java)
//            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            intent.putExtra(EXTRA_FACE_PROBLEM, faceProblem)
            context.startActivity(intent)
        }
    }

    private var getProduct = MutableLiveData<FaceProblemModel>()
    private lateinit var binding: ActivityFaceProblemBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
        retrieveData()
        observerData()
    }

    private fun initBinding() {
        binding = ActivityFaceProblemBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    private fun observerData() {
        getProduct.observe(this) {
            updateView(it)
        }
    }

    private fun updateView(product: FaceProblemModel?) {
//        Glide.with(this)
//            .load(product?.)
//            .into(binding.ivImage)
        binding.tvName.text = product?.nama
        binding.tvPenjelasan.text = product?.penjelasan
    }

    private fun retrieveData() {
        getProduct.value = intent.extras?.getParcelable(EXTRA_FACE_PROBLEM)
        Log.d(TAG, "retrieveData $getProduct")
    }
}