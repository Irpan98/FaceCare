package id.itborneo.facecare.core.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProductModel(
    val nama: String = "",
    val url_image: String = "",
    val link: String = "",
    val penjelasan: String = "",
    val id: Int = 0
) : Parcelable
