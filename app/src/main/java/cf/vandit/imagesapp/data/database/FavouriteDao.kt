package cf.vandit.imagesapp.data.database

import androidx.room.*
import cf.vandit.imagesapp.data.models.ImageData

@Dao
interface FavouriteDao {
    @Insert
    suspend fun insertImage(favourite: ImageData)

    @Update
    suspend fun updateImage(favourite: ImageData)

    @Delete
    suspend fun deleteImage(favourite: ImageData)

    @Query("SELECT * FROM imagedata")
    fun getAllImages(): List<ImageData>

    @Query("SELECT * FROM imagedata WHERE id = :id")
    suspend fun getImage(id:String): ImageData?
}