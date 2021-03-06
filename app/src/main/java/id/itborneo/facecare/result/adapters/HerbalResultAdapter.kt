package id.itborneo.facecare.result.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.itborneo.facecare.core.model.HerbalModel
import id.itborneo.facecare.databinding.ItemResultFaceProblemBinding


class HerbalResultAdapter(private val listener: (HerbalModel) -> Unit) :
    RecyclerView.Adapter<HerbalResultAdapter.ViewHolder>() {

    private var movies = listOf<HerbalModel>()

    fun set(data: List<HerbalModel>) {
        this.movies = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding =
            ItemResultFaceProblemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(movies[position])
    }

    override fun getItemCount() = movies.size

    inner class ViewHolder(private val itemBinding: ItemResultFaceProblemBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(faceProblem: HerbalModel) {
            itemBinding.apply {
                tvName.text = faceProblem.nama
//                tvName.text = movie.title
//                Glide.with(root.context)
//                    .load("${ImageConstant.BASE_IMAGE}${movie.posterPath}")
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