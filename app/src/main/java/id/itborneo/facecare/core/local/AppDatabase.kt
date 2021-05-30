package id.itborneo.facecare.core.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import id.itborneo.facecare.core.model.ResultModel

@Database(
    entities = [ResultModel::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun resultDao(): FaceCareDao

    companion object {
        private var INSTANCE: AppDatabase? = null

        private const val TABLE_NAME = "db_result_users"

        fun getInstance(context: Context): AppDatabase {
            if (INSTANCE == null) {
                synchronized(this) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        TABLE_NAME
                    ).build()
                }
            }
            return INSTANCE as AppDatabase
        }
    }
}