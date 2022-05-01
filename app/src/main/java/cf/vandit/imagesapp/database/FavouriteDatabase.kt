package cf.vandit.imagesapp.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Favourite::class], version = 1)
abstract class FavouriteDatabase: RoomDatabase() {
    abstract fun favouriteDao() : FavouriteDao
}