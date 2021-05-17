package id.itborneo.facecare.article

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import id.itborneo.facecare.databinding.FragmentArticleBinding
import id.itborneo.facecare.model.ArticleModel


class ArticleFragment : Fragment() {

    private lateinit var binding: FragmentArticleBinding
    private lateinit var adapter: ArticleAdapter

    companion object {
        private const val TAG = "ArticleFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentArticleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecycler()
        observerData()
    }


    private fun initRecycler() {
        binding.rvArticle.layoutManager = LinearLayoutManager(requireContext())
        adapter = ArticleAdapter {
//            actionToDetail(it)
        }
        binding.rvArticle.adapter = adapter
    }

    private fun observerData() {

        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("artikel")


        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val map = dataSnapshot.value as Map<String, Any>?

                val faceProblems = mutableListOf<ArticleModel>()
                Log.d(TAG, "Value is: $map")

                dataSnapshot.children.forEachIndexed { index, snapshot ->
                    val data =
                        snapshot.getValue(ArticleModel::class.java)
                    if (data != null) {
                        faceProblems.add(data)
                    }

                }


                adapter.set(faceProblems)

            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })
    }

}