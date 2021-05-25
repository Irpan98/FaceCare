package id.itborneo.facecare.core.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class HerbalModel(
    val nama: String = "",
    val url_image: String = "",
    val penjelasan: String = "",
    val how_to_use: String = "",
    val id: Int = 0
) : Parcelable
