package id.itborneo.facecare.herbal

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import id.itborneo.facecare.core.model.HerbalModel
import id.itborneo.facecare.databinding.ActivityHerbalBinding

class HerbalActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "HerbalActivity"
        private const val EXTRA_HERBAL = "extra_herbal"

        fun getInstance(context: Context, product: List<HerbalModel>) {
            val intent = Intent(context, HerbalActivity::class.java)
//            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            intent.putParcelableArrayListExtra(
                EXTRA_HERBAL,
                toArraylist(product)
            )
            context.startActivity(intent)
        }

        private fun toArraylist(list: List<HerbalModel>): ArrayList<HerbalModel> {
            val arrayListResult = ArrayList<HerbalModel>()
            list.forEach {
                arrayListResult.add(it)
            }

            return arrayListResult
        }
    }


    private var getProduct = MutableLiveData<ArrayList<HerbalModel>>()
    private lateinit var binding: ActivityHerbalBinding
    private lateinit var ProductAdapter: HerbalAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
        initRecycler()
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
            ProductAdapter.set(it)
        }
    }


    private fun retrieveData() {
        getProduct.value = intent.extras?.getParcelableArrayList(EXTRA_HERBAL)
        Log.d(TAG, "retrieveData $getProduct")
    }

    private fun initRecycler() {


        binding.rvProduct.layoutManager = LinearLayoutManager(this)
        ProductAdapter = HerbalAdapter {
        }
        binding.rvProduct.adapter = ProductAdapter

    }
}