package id.itborneo.facecare.product

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import id.itborneo.facecare.R
import id.itborneo.facecare.core.model.ProductModel
import id.itborneo.facecare.databinding.ItemProductBinding


class ProductAdapter(private val listener: (ProductModel) -> Unit) :
    RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    private var list = listOf<ProductModel>()

    fun set(list: List<ProductModel>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding =
            ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount() = list.size

    inner class ViewHolder(private val itemBinding: ItemProductBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(faceProblem: ProductModel) {
            itemBinding.apply {
                tvName.text = faceProblem.nama
                tvDescription.text = faceProblem.penjelasan
                Glide.with(root.context)
                    .load(faceProblem.url_image)
                    .placeholder(R.drawable.ic_image_placeholder)
                    .transform(CenterCrop(), RoundedCorners(20))
                    .into(ivImage)
                root.setOnClickListener {
                    listener(faceProblem)
                }
            }
        }
    }
}