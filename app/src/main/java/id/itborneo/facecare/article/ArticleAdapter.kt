package id.itborneo.facecare.article

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.itborneo.facecare.core.model.ArticleModel
import id.itborneo.facecare.databinding.ItemArticleBinding
import id.itborneo.facecare.databinding.ItemResultFaceProblemBinding


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
//                Glide.with(root.context)
//                    .load("${ImageConstant.BASE_IMAGE}${movie.posterPath}")
//                    .placeholder(R.drawable.ic_placeholder_image)
//                    .transform(CenterCrop(), RoundedCorners(ImageConstant.IMAGE_RADIUS))
//                    .into(ivPoster)
//                root.setOnClickListener {
//                    listener(movie)
//                }
            }
        }
    }
}