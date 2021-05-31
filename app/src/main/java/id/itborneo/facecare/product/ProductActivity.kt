package id.itborneo.facecare.product

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import id.itborneo.facecare.core.model.ProductModel
import id.itborneo.facecare.databinding.ActivityProductBinding

class ProductActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "ProductActivity"
        private const val EXTRA_PRODUCT = "extra_product"

        fun getInstance(context: Context, product: List<ProductModel>) {
            val intent = Intent(context, ProductActivity::class.java)
//            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            intent.putParcelableArrayListExtra(EXTRA_PRODUCT, toArraylist(product))
            context.startActivity(intent)
        }

        private fun toArraylist(list: List<ProductModel>): ArrayList<ProductModel> {
            val arrayListResult = ArrayList<ProductModel>()
            list.forEach {
                arrayListResult.add(it)
            }

            return arrayListResult
        }
    }

    private var getFaceProblems = MutableLiveData<ArrayList<ProductModel>>()
    private lateinit var binding: ActivityProductBinding
    private lateinit var ProductAdapter: ProductAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
        initRecycler()
        retrieveData()
        observerData()
    }

    private fun initBinding() {
        binding = ActivityProductBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }


    private fun retrieveData() {
        getFaceProblems.value = intent.extras?.getParcelableArrayList(EXTRA_PRODUCT)
        Log.d(TAG, "retrieveData ${getFaceProblems.value}")
    }

    private fun initRecycler() {

        binding.rvProduct.layoutManager = LinearLayoutManager(this)
        ProductAdapter = ProductAdapter {
        }
        binding.rvProduct.adapter = ProductAdapter

    }

    private fun observerData() {
        getFaceProblems.observe(this) {
            ProductAdapter.set(it)
        }
    }
}