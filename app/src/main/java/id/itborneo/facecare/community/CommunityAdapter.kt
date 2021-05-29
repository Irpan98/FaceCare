package id.itborneo.facecare.community

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.itborneo.facecare.core.model.MessageModel
import id.itborneo.facecare.databinding.ItemArticleBinding


class CommunityAdapter(private val listener: (MessageModel) -> Unit) :
    RecyclerView.Adapter<CommunityAdapter.ViewHolder>() {

    private var list = listOf<MessageModel>()

    fun set(list: List<MessageModel>) {
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
        fun bind(message: MessageModel) {
            itemBinding.apply {
                tvName.text = message.senderName
                tvDescription.text = message.text
//                Glide.with(root.context)
//                    .load("${ImageConstant.BASE_IMAGE}${movie.posterPath}")
//                    .placeholder(R.drawable.ic_placeholder_image)
//                    .transform(CenterCrop(), RoundedCorners(ImageConstant.IMAGE_RADIUS))
//                    .into(ivPoster)
                root.setOnClickListener {
                    listener(message)
                }
            }
        }
    }
}