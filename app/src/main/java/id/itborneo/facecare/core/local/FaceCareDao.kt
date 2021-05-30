package id.itborneo.facecare.core.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import id.itborneo.facecare.core.model.ResultModel

@Dao
interface FaceCareDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(result: ResultModel): Long

//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    fun addSeriesFavorite(favoriteSeries: FavoriteSeriesEntity): Long
//
//    @Query("SELECT * FROM movie_favorite WHERE id=:id ")
//    fun getSingleMovieFavorite(id: Int): FavoriteMovieEntity?
//
//    @Query("SELECT * FROM Series_favorite WHERE id=:id ")
//    fun getSingleSeriesFavorite(id: Int): FavoriteSeriesEntity?
//
//    @Delete
//    fun removeMovieFavorite(favoriteMovie: FavoriteMovieEntity): Int
//
//    @Delete
//    fun removeSeriesFavorite(favoriteSeries: FavoriteSeriesEntity) : Int

    @Query("SELECT * FROM result")
    fun getResults(): List<ResultModel>
//
//    @Query("SELECT * FROM series_favorite")
//    fun getSeriesFavorites(): DataSource.Factory<Int, FavoriteSeriesEntity>


}