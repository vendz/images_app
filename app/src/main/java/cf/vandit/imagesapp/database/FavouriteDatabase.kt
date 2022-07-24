package cf.vandit.imagesapp.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import cf.vandit.imagesapp.network.ImageData
import cf.vandit.imagesapp.network.ImageDataTypeConverter

@Database(entities = [ImageData::class], version = 1)
@TypeConverters(ImageDataTypeConverter::class)
abstract class FavouriteDatabase: RoomDatabase() {
    abstract fun favouriteDao() : FavouriteDao
}