package id.itborneo.facecare.core.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RecognitionModel(
    var id: String = "",
    var title: String = "",
    var confidence: Float = 0f
): Parcelable