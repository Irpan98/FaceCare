package id.itborneo.facecare.article.views

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import id.itborneo.facecare.R
import id.itborneo.facecare.core.model.ArticleModel
import id.itborneo.facecare.databinding.ActivityDetailArticleBinding

class DetailArticleActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "DetailArticleActivity"
        private const val EXTRA_ARTICLE = "extra_face_problem"

        fun getInstance(context: Context, faceProblem: ArticleModel) {
            val intent = Intent(context, DetailArticleActivity::class.java)
//            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            intent.putExtra(EXTRA_ARTICLE, faceProblem)
            context.startActivity(intent)
        }
    }

    private var getProduct = MutableLiveData<ArticleModel>()
    private lateinit var binding: ActivityDetailArticleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
        retrieveData()
        observerData()
    }

    private fun initBinding() {
        binding = ActivityDetailArticleBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    private fun observerData() {
        getProduct.observe(this) {
            updateView(it)
        }
    }

    private fun updateView(article: ArticleModel?) {
        Glide.with(this)
            .load(article?.gambar)
            .placeholder(R.drawable.ic_image_placeholder)
            .into(binding.ivImage)
        binding.tvName.text = article?.judul
        binding.tvPenjelasan.text = article?.penjelasan
    }

    private fun retrieveData() {
        getProduct.value = intent.extras?.getParcelable(EXTRA_ARTICLE)
        Log.d(TAG, "retrieveData $getProduct")
    }
}