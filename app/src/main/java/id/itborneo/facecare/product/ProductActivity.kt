package id.itborneo.facecare.product

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import id.itborneo.facecare.core.model.ProductModel
import id.itborneo.facecare.databinding.ActivityProductBinding

class ProductActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "ProductActivity"
        private const val EXTRA_PRODUCT = "extra_product"

        fun getInstance(context: Context, product: ProductModel) {
            val intent = Intent(context, ProductActivity::class.java)
//            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            intent.putExtra(EXTRA_PRODUCT, product)
            context.startActivity(intent)
        }
    }

    private var getProduct = MutableLiveData<ProductModel>()
    private lateinit var binding: ActivityProductBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
        retrieveData()
        observerData()
    }

    private fun initBinding() {
        binding = ActivityProductBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    private fun observerData() {
        getProduct.observe(this) {
            updateView(it)
        }
    }

    private fun updateView(product: ProductModel?) {
        Glide.with(this)
            .load(product?.url_image)
            .into(binding.ivImage)
        binding.tvName.text = product?.nama
        binding.tvPenjelasan.text = product?.penjelasan
    }

    private fun retrieveData() {
        getProduct.value = intent.extras?.getParcelable(EXTRA_PRODUCT)
        Log.d(TAG, "retrieveData $getProduct")
    }
}