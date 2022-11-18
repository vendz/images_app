package cf.vandit.imagesapp.data.database

import androidx.lifecycle.LiveData
import androidx.room.*
import cf.vandit.imagesapp.data.models.ImageData

@Dao
interface FavouriteDao {
    @Insert
    suspend fun insertImage(favourite: ImageData)

    @Delete
    suspend fun deleteImage(favourite: ImageData)

    @Query("SELECT * FROM imagedata")
    fun getAllImages(): LiveData<List<ImageData>>

    @Query("SELECT * FROM imagedata WHERE id = :id")
    suspend fun getImage(id:String): ImageData?
}