package id.itborneo.facecare.community

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import id.itborneo.facecare.R
import id.itborneo.facecare.core.model.MessageModel
import id.itborneo.facecare.databinding.ItemArticleBinding
import id.itborneo.facecare.databinding.ItemChatBinding


class CommunityAdapter(private val listener: (MessageModel) -> Unit) :
    RecyclerView.Adapter<CommunityAdapter.ViewHolder>() {

    private var list = listOf<MessageModel>()

    fun set(list: List<MessageModel>) {
        this.list = list
        notifyDataSetChanged()

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding =
            ItemChatBinding .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount() = list.size

    inner class ViewHolder(private val itemBinding: ItemChatBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(message: MessageModel) {
            itemBinding.apply {
                tvName.text = message.senderName
                tvDescription.text = message.text
                Glide.with(root.context)
                    .load(R.drawable.ic_girl_user)
                    .placeholder(R.drawable.ic_girl_user)
                    .into(imageView2)
                root.setOnClickListener {
                    listener(message)
                }
            }
        }
    }
}