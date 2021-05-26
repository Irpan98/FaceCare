package id.itborneo.facecare.core.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ArticleModel(
    val judul: String = "",
    val penjelasan: String = "",
    val referensi: String = "",
    val gambar: String = ""
) : Parcelable
