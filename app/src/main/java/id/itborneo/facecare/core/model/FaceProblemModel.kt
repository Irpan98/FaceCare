package id.itborneo.facecare.core.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FaceProblemModel(
    val nama: String = "",
    val penjelasan: String = "",
    val solusi_herbal: MutableList<HerbalModel> = mutableListOf(),
    val solusi_produk: MutableList<ProductModel> = mutableListOf()
) : Parcelable
