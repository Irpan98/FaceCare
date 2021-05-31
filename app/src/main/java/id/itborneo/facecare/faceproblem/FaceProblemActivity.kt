package id.itborneo.facecare.faceproblem

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import id.itborneo.facecare.core.model.FaceProblemModel
import id.itborneo.facecare.databinding.ActivityFaceProblemBinding

class FaceProblemActivity : AppCompatActivity() {

    private lateinit var faceProblemAdapter: FaceProblemAdapter

    companion object {
        private const val TAG = "FaceProblemActivity"
        private const val EXTRA_FACE_PROBLEM = "extra_face_problem"

        fun getInstance(context: Context, faceProblem: List<FaceProblemModel>) {
            val intent = Intent(context, FaceProblemActivity::class.java)
//            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            intent.putParcelableArrayListExtra(
                EXTRA_FACE_PROBLEM, toArraylist(faceProblem)
            )
            context.startActivity(intent)
        }

        private fun toArraylist(list: List<FaceProblemModel>): ArrayList<FaceProblemModel> {
            val arrayListResult = ArrayList<FaceProblemModel>()
            list.forEach {
                val data = FaceProblemModel(
                    it.nama,
                    it.penjelasan,
                    it.solusi_herbal,
                    it.solusi_produk
                )
                arrayListResult.add(data)
            }

            return arrayListResult
        }
    }

    private var getFaceProblems = MutableLiveData<ArrayList<FaceProblemModel>>()
    private lateinit var binding: ActivityFaceProblemBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
        initRecyclerFaceProblem()
        retrieveData()
        observerData()
    }

    private fun initBinding() {
        binding = ActivityFaceProblemBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    private fun observerData() {
        getFaceProblems.observe(this) {
            faceProblemAdapter.set(it)
        }
    }

    private fun retrieveData() {
        getFaceProblems.value = intent.extras?.getParcelableArrayList(EXTRA_FACE_PROBLEM)
        Log.d(TAG, "retrieveData ${getFaceProblems.value}")
    }

    private fun initRecyclerFaceProblem() {


        binding.rvResultFaceProblem.layoutManager = LinearLayoutManager(this)
        faceProblemAdapter = FaceProblemAdapter {
        }
        binding.rvResultFaceProblem.adapter = faceProblemAdapter

    }


}