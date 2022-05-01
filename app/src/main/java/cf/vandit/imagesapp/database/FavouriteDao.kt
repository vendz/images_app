package cf.vandit.imagesapp.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FavouriteDao {
    @Insert
    suspend fun insertImage(favourite: Favourite)

    @Update
    suspend fun updateImage(favourite: Favourite)

    @Delete
    suspend fun deleteImage(favourite: Favourite)

    @Query("SELECT * FROM favourite")
    fun getAllImages(): LiveData<List<Favourite>>
}