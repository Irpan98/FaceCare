package id.itborneo.facecare.herbal

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import id.itborneo.facecare.core.model.HerbalModel
import id.itborneo.facecare.databinding.ActivityHerbalBinding
import id.itborneo.facecare.product.ProductActivity

class HerbalActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "HerbalActivity"
        private const val EXTRA_HERBAL = "extra_herbal"

        fun getInstance(context: Context, product: HerbalModel) {
            val intent = Intent(context, HerbalActivity::class.java)
//            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            intent.putExtra(EXTRA_HERBAL, product)
            context.startActivity(intent)
        }
    }


    private var getProduct = MutableLiveData<HerbalModel>()
    private lateinit var binding: ActivityHerbalBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
        retrieveData()
        observerData()
    }

    private fun initBinding() {
        binding = ActivityHerbalBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    private fun observerData() {
        getProduct.observe(this) {
            updateView(it)
        }
    }

    private fun updateView(product: HerbalModel?) {
        Glide.with(this)
            .load(product?.url_image)
            .into(binding.ivImage)
        binding.tvName.text = product?.nama
        binding.tvPenjelasan.text = product?.penjelasan
    }

    private fun retrieveData() {
        getProduct.value = intent.extras?.getParcelable(EXTRA_HERBAL)
        Log.d(TAG, "retrieveData $getProduct")
    }
}