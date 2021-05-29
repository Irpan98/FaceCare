package id.itborneo.facecare.core.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserInfoModel(

    var name: String = ""
) : Parcelable
