package id.itborneo.facecare.result

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import id.itborneo.facecare.core.factory.ViewModelFactory
import id.itborneo.facecare.core.model.FaceProblemModel
import id.itborneo.facecare.core.model.HerbalModel
import id.itborneo.facecare.core.model.ProductModel
import id.itborneo.facecare.core.model.RecognitionModel
import id.itborneo.facecare.databinding.ActivityResultBinding
import id.itborneo.facecare.faceproblem.FaceProblemActivity
import id.itborneo.facecare.herbal.HerbalActivity
import id.itborneo.facecare.product.ProductActivity
import id.itborneo.facecare.result.adapters.FaceProblemResultAdapter
import id.itborneo.facecare.result.adapters.HerbalResultAdapter
import id.itborneo.facecare.result.adapters.ProductResultAdapter
import id.itborneo.facecare.utils.enums.Status


class ResultActivity : AppCompatActivity() {


    companion object {
        private const val TAG = "ResultActivity"

        private val EXTRA_LIST = "list_value"
        fun getInstance(context: Context, value: ArrayList<RecognitionModel>?) {
            val intent = Intent(context, ResultActivity::class.java)
            intent.putParcelableArrayListExtra(EXTRA_LIST, value)
//            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            context.startActivity(intent)
        }
    }

    private lateinit var binding: ActivityResultBinding

    private val viewModel: ResultViewModel by viewModels {
        val faceproblem = "testing"
        ViewModelFactory(faceproblem)
    }

    private lateinit var faceProblemAdapter: FaceProblemResultAdapter
    private lateinit var herbalAdapter: HerbalResultAdapter
    private lateinit var productAdapter: ProductResultAdapter
    private var getListResult = MutableLiveData<ArrayList<RecognitionModel>?>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
        initRecyclerFaceProblem()
        initRecyclerNaturalIngredient()
        initRecyclerProduct()

        retrieveData()
        observerGetList()

    }

    private fun observerGetList() {
        getListResult.observe(this) {
            if (it != null) {
                observerData(it)
            } else {
                Toast.makeText(this, "Something's Wrong", Toast.LENGTH_LONG).show()
            }

        }
    }

    private fun retrieveData() {
        getListResult.value = intent.getParcelableArrayListExtra(EXTRA_LIST)
        Log.d(TAG, "retrieveData ${getListResult.value}")
    }

    private fun initBinding() {
        binding = ActivityResultBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    private fun observerData(userProblems: ArrayList<RecognitionModel>) {
        viewModel.getListFaceProblems().observe(this) {

            val solusiHerbal = mutableListOf<HerbalModel>()
            val products = mutableListOf<ProductModel>()
            when (it.status) {
                Status.SUCCESS -> {
                    val faceProblems = it.data ?: return@observe

                    val usersFaceProblems = faceProblems.filterIndexed { index, item ->
                        var result = false
                        userProblems.forEach {
                            Log.d(
                                TAG,
                                "faceProblems filterIndexed each $index = ${it.title} compare with ${item.nama}"
                            )

                            if (it.title.equals(item.nama, ignoreCase = true)) {
                                result = true
                                return@forEach
                            }
                        }
                        Log.d(TAG, "faceProblems filterIndexed each $index =  $result")

                        result

                    }

                    Log.d(TAG, "faceProblems filtered $usersFaceProblems")
                    updateUI(usersFaceProblems, solusiHerbal, products)

                }
            }
        }
    }

    private fun updateUI(
        faceProblems: List<FaceProblemModel>,
        solusiHerbal: MutableList<HerbalModel>,
        products: MutableList<ProductModel>
    ) {

        faceProblemAdapter.set(faceProblems)

        faceProblems.forEach { faceProblem ->
            faceProblem.solusi_herbal.let { solusiHerbal.addAll(it) }
            faceProblem.solusi_produk.let { products.addAll(it) }
        }

        updateHerbalView(solusiHerbal.distinct())
        updateProductView(products.distinct())
    }

    private fun updateHerbalView(herbalModel: List<HerbalModel>) {
        Log.d(TAG, "observerNaturalIngredient $herbalModel")

        herbalAdapter.set(herbalModel)

    }

    private fun updateProductView(products: List<ProductModel>) {
        Log.d(TAG, "observerProduct $products")

        productAdapter.set(products)

    }

    private fun initRecyclerFaceProblem() {
//        binding.rvResultFaceProblem.layoutManager = LinearLayoutManager(this)
        faceProblemAdapter = FaceProblemResultAdapter {
            actionToFaceProblemDetail(it)
        }
//        binding.rvResultFaceProblem.adapter = faceProblemAdapter
    }

    private fun initRecyclerNaturalIngredient() {
//        binding.rvResultNaturalIngredient.layoutManager =
            LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)

        herbalAdapter = HerbalResultAdapter {
            actionToHerbalDetail(it)
        }
//        binding.rvResultNaturalIngredient.adapter = herbalAdapter
    }

    private fun initRecyclerProduct() {
//        binding.rvResultProduct.layoutManager =
            LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)

        productAdapter = ProductResultAdapter {
            actionToProductDetail(it)
        }
//        binding.rvResultProduct.adapter = productAdapter
    }

    private fun actionToProductDetail(product: ProductModel) {
        ProductActivity.getInstance(this, product)
    }

    private fun actionToHerbalDetail(herbal: HerbalModel) {
        HerbalActivity.getInstance(this, herbal)
    }

    private fun actionToFaceProblemDetail(faceProblem: FaceProblemModel) {
        FaceProblemActivity.getInstance(this, faceProblem)
    }

}