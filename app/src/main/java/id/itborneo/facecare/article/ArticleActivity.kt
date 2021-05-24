package id.itborneo.facecare.article

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import id.itborneo.facecare.core.ThisRepository
import id.itborneo.facecare.databinding.ActivityArticleBinding
import id.itborneo.facecare.utils.enums.Status


class ArticleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityArticleBinding
    private lateinit var adapter: ArticleAdapter

    companion object {
        private const val TAG = "ArticleActivity"

        fun getInstance(context: Context) {
            val intent = Intent(context, ArticleActivity::class.java)
//            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            context.startActivity(intent)
        }
    }

//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        binding = FragmentArticleBinding.inflate(inflater, container, false)
//        return binding.root
//    }


    private fun initBinding() {
        binding = ActivityArticleBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBinding()
        initRecycler()
        observerData()
    }


    private fun initRecycler() {
        binding.rvArticle.layoutManager = LinearLayoutManager(this)
        adapter = ArticleAdapter {
//            actionToDetail(it)
        }
        binding.rvArticle.adapter = adapter
    }

    private fun observerData() {


        ThisRepository().getArticle().observe(this) {

            when (it.status) {
                Status.SUCCESS -> {
                    val data = it.data
                    if (data != null) {
                        adapter.set(data)
                    }
                }
            }

        }

//        val database = FirebaseDatabase.getInstance()
//        val myRef = database.getReference("artikel")
//
//
//        myRef.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//
//                val articles = mutableListOf<ArticleModel>()
//                dataSnapshot.children.forEachIndexed { index, snapshot ->
//                    val data = snapshot.getValue(ArticleModel::class.java)
//                    if (data != null) {
//                        articles.add(data)
//                    }
//                }
//
//
//                adapter.set(articles)
//
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                // Failed to read value
//                Log.w(TAG, "Failed to read value.", error.toException())
//            }
//        })
    }

}