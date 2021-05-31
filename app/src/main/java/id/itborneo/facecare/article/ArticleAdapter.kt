package id.itborneo.facecare.article

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import id.itborneo.facecare.R
import id.itborneo.facecare.core.model.ArticleModel
import id.itborneo.facecare.databinding.ItemArticleBinding


class ArticleAdapter(private val listener: (ArticleModel) -> Unit) :
    RecyclerView.Adapter<ArticleAdapter.ViewHolder>() {

    private var list = listOf<ArticleModel>()

    fun set(list: List<ArticleModel>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding =
            ItemArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount() = list.size

    inner class ViewHolder(private val itemBinding: ItemArticleBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(article: ArticleModel) {
            itemBinding.apply {
                tvName.text = article.judul
//                tvName.text = movie.title
                Glide.with(root.context)
                    .load(article.gambar)
                    .placeholder(R.drawable.ic_image_placeholder)
                    .transform(CenterCrop(), RoundedCorners(20))
                    .into(imageView2)
                root.setOnClickListener {
                    listener(article)
                }
            }
        }
    }
}