package id.itborneo.facecare.result

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import id.itborneo.facecare.databinding.ActivityResultBinding
import id.itborneo.facecare.model.FaceProblemModel
import id.itborneo.facecare.model.NaturalIngredientModel
import id.itborneo.facecare.model.ProductModel
import id.itborneo.facecare.result.adapters.FaceProblemResultAdapter
import id.itborneo.facecare.result.adapters.NaturalIngredientResultAdapter
import id.itborneo.facecare.result.adapters.ProductResultAdapter


class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding

    private lateinit var faceProblemAdapter: FaceProblemResultAdapter
    private lateinit var naturalIngredientAdapter: NaturalIngredientResultAdapter
    private lateinit var productAdapter: ProductResultAdapter

    companion object {
        private const val TAG = "ResultActivity"

        fun getInstance(context: Context) {
            val intent = Intent(context, ResultActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        initBinding()
        initRecyclerFaceProblem()
        initRecyclerNaturalIngredient()
        initRecyclerProduct()
        dataDummy()
        observerData()

    }

    private fun dataDummy() {

//        val data = listOf(
//            FaceProblemModel("jerawat", "abc"),
//            FaceProblemModel("acne a", "asdasdsad"),
//            FaceProblemModel("acne b", "asdasdaw")
//        )

//        faceProblemAdapter.set(data)
//
//        val data2 = listOf(
//            NaturalIngredientModel("Jambu", "abc", "", "", "", "", 0),
//            NaturalIngredientModel("Jambu", "abc", "", "", "", "", 0),
//            NaturalIngredientModel("Jambu", "abc", "", "", "", "", 0),
//        )

//        naturalIngredientAdapter.set(data2)


        val data3 = listOf(
            ProductModel("Ponds", "abc", "", "", 0),
            ProductModel("Ponds 2", "abc", "", "", 0),
            ProductModel("Ponds 3", "abc", "", "", 0),
        )

        productAdapter.set(data3)

    }

    private fun initBinding() {
        binding = ActivityResultBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    private fun observerData() {

        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("identifikasi")


        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val map = dataSnapshot.value as Map<String, Any>?

                val faceProblems = mutableListOf<FaceProblemModel>()
                Log.d(TAG, "Value is: $map")

                dataSnapshot.children.forEachIndexed { index, snapshot ->
                    val faceProblem =
                        snapshot.getValue(FaceProblemModel::class.java)
                    if (faceProblem != null) {
                        faceProblems.add(faceProblem)
                    }

                }


                faceProblemAdapter.set(faceProblems)

                //dummy
                val natural = faceProblems[0].solusi_herbal_1.distinct()
                observerNaturalIngredient(natural)


                Log.d(TAG, "Value object is: $faceProblems")
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })
    }

    private fun observerNaturalIngredient(naturalIngredientModel: List<NaturalIngredientModel>) {

        naturalIngredientAdapter.set(naturalIngredientModel)

    }

    private fun initRecyclerFaceProblem() {
        binding.rvResultFaceProblem.layoutManager = LinearLayoutManager(this)
        faceProblemAdapter = FaceProblemResultAdapter {
//            actionToDetail(it)
        }
        binding.rvResultFaceProblem.adapter = faceProblemAdapter
    }

    private fun initRecyclerNaturalIngredient() {
        binding.rvResultNaturalIngredient.layoutManager = LinearLayoutManager(this)
        naturalIngredientAdapter = NaturalIngredientResultAdapter {
//            actionToDetail(it)
        }
        binding.rvResultNaturalIngredient.adapter = naturalIngredientAdapter
    }

    private fun initRecyclerProduct() {
        binding.rvResultProduct.layoutManager = LinearLayoutManager(this)
        productAdapter = ProductResultAdapter {
//            actionToDetail(it)
        }
        binding.rvResultProduct.adapter = productAdapter
    }
}