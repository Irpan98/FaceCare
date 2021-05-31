package id.itborneo.facecare.faceproblem

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.itborneo.facecare.core.model.FaceProblemModel
import id.itborneo.facecare.databinding.ItemFaceProblemBinding


class FaceProblemAdapter(private val listener: (FaceProblemModel) -> Unit) :
    RecyclerView.Adapter<FaceProblemAdapter.ViewHolder>() {

    private var list = listOf<FaceProblemModel>()

    fun set(list: List<FaceProblemModel>) {
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
        fun bind(faceProblem: FaceProblemModel) {
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