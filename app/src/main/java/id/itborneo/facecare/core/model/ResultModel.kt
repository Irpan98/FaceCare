package id.itborneo.facecare.core.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "result")
data class ResultModel(

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,

    @ColumnInfo(name = "image_url")
    var image_url: String = "",

    @ColumnInfo(name = "date")
    var date: String = ""
)
