package id.itborneo.facecare.daytoday

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import id.itborneo.facecare.core.model.ResultModel
import id.itborneo.facecare.databinding.ItemDayToDayBinding


class DayToDayAdapter(private val listener: (ResultModel) -> Unit) :
    RecyclerView.Adapter<DayToDayAdapter.ViewHolder>() {

    private var list = listOf<ResultModel>()

    fun set(list: List<ResultModel>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding =
            ItemDayToDayBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount() = list.size

    inner class ViewHolder(private val itemBinding: ItemDayToDayBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(article: ResultModel) {
            itemBinding.apply {
//                ivImage.text = article.judul
//                tvName.text = movie.title
                Glide.with(root.context)
                    .load(Uri.parse(article.image_url))
//                    .placeholder(R.drawable.ic_placeholder_image)
                    .transform(CenterCrop(), RoundedCorners(20))
                    .into(ivImage)
                root.setOnClickListener {
                    listener(article)
                }
            }
        }
    }
}