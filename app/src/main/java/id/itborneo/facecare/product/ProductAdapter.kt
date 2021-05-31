package id.itborneo.facecare.product

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.itborneo.facecare.core.model.ProductModel
import id.itborneo.facecare.databinding.ItemFaceProblemBinding


class ProductAdapter(private val listener: (ProductModel) -> Unit) :
    RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    private var list = listOf<ProductModel>()

    fun set(list: List<ProductModel>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding =
            ItemFaceProblemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount() = list.size

    inner class ViewHolder(private val itemBinding: ItemFaceProblemBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(faceProblem: ProductModel) {
            itemBinding.apply {
                tvName.text = faceProblem.nama
                tvDescription.text = faceProblem.penjelasan
//                Glide.with(root.context)
//                    .load(faceProblem.)
//                    .placeholder(R.drawable.ic_placeholder_image)
//                    .transform(CenterCrop(), RoundedCorners(ImageConstant.IMAGE_RADIUS))
//                    .into(ivPoster)
                root.setOnClickListener {
                    listener(faceProblem)
                }
            }
        }
    }
}