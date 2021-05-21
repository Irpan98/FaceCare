package id.itborneo.facecare.identify

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.RadioButton
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import id.itborneo.facecare.R
import id.itborneo.facecare.databinding.ActivityIdentifyBinding
import id.itborneo.facecare.model.FaceProblemModel
import id.itborneo.facecare.utils.KsPrefUser


class IdentifyActivity : AppCompatActivity() {


    private val viewModel: IndentifyViewModel by viewModels()

    companion object {
        private const val TAG = "IdentifyActivity"

        fun getInstance(context: Context) {
            val intent = Intent(context, IdentifyActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            context.startActivity(intent)
        }
    }

    private lateinit var binding: ActivityIdentifyBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
        buttonListener()
        initFaceProblem()
        initGenderView()
        initSkinType()
        observerData()
    }

    private fun initSkinType() {

        val skinTypes = listOf("Oily", "Dry", "Combination", "Normal")
        val listId = mutableListOf<Int>()

        val rgp = binding.rgSkinType
        rgp.orientation = LinearLayout.HORIZONTAL

        skinTypes.forEach { i ->
            val rbn = RadioButton(this)
            rbn.id = View.generateViewId()
            rbn.text = "${i}"
            val params =
                LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1f
                )
            rbn.layoutParams = params
            rgp.addView(rbn)
            listId.add(rbn.id)
        }

        binding.rgSkinType.setOnCheckedChangeListener { group, checkedId ->

            listId.forEachIndexed { index, value ->
                if (checkedId == value) {
                    viewModel.userIndentified.value?.skinType = skinTypes[index]
                    return@forEachIndexed
                }
            }
        }
    }

    private fun initGenderView() {
        // To listen for a radio button's checked/unchecked state changes
        binding.rgGender.setOnCheckedChangeListener { group, checkedId ->

            when (checkedId) {
                R.id.rgGenderGirls -> {
                    Log.d(TAG, "initGenderView its girls")
                    viewModel.userIndentified.value?.gender = "Female"
                }
                else -> {
                    Log.d(TAG, "initGenderView its boys")
                    viewModel.userIndentified.value?.gender = "Male"

                }
            }
        }
    }

    private fun initFaceProblem() {

        val listId = mutableListOf<Int>()

        val radioFaceProblem = binding.rgFaceProblem
        radioFaceProblem.orientation = LinearLayout.VERTICAL

        faceProblems.observe(this) {

            it.forEach { i ->
                val rbn = RadioButton(this)
                rbn.id = View.generateViewId()
                rbn.text = "${i.nama}"
                val params =
                    LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1f
                    )
                rbn.layoutParams = params
                radioFaceProblem.addView(rbn)
                listId.add(rbn.id)
            }
        }

        binding.rgFaceProblem.setOnCheckedChangeListener { group, checkedId ->

            listId.forEachIndexed { index, value ->
                if (checkedId == value) {
                    val selectedFaceProblem = faceProblems.value?.get(index)
                    Log.d(TAG, "rgFaceProblem $selectedFaceProblem")

                    viewModel.userIndentified.value?.faceProblem = selectedFaceProblem?.nama ?: ""
                    return@forEachIndexed
                }
            }
        }


    }


    val faceProblems = MutableLiveData<List<FaceProblemModel>>()

    private fun observerData() {

        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("identifikasi")


        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val map = dataSnapshot.value as Map<String, Any>?

                val getFaceProblems = mutableListOf<FaceProblemModel>()
                Log.d(TAG, "Value is: $map")

                dataSnapshot.children.forEachIndexed { index, snapshot ->
                    val faceProblem =
                        snapshot.getValue(FaceProblemModel::class.java)
                    if (faceProblem != null) {
                        getFaceProblems.add(faceProblem)
                    }

                }


                faceProblems.value = getFaceProblems



                Log.d(TAG, "Value object is: $faceProblems")
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })
    }


    private fun buttonListener() {


        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("usersIdentified")


        binding.btnSubmit.setOnClickListener {


            val identify = viewModel.userIndentified.value

            val userId = KsPrefUser.getUser()
            myRef.child(userId).setValue(identify)
                .addOnSuccessListener {
                    Log.d(TAG, "success add data : $it")

                }
                .addOnFailureListener {
                    Log.e(TAG, "error add data ${it.message}")

                }

        }
    }

    private fun initBinding() {
        binding = ActivityIdentifyBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }


}