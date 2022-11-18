package cf.vandit.imagesapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import cf.vandit.imagesapp.data.models.ImageData
import cf.vandit.imagesapp.data.models.ImageDataTypeConverter

@Database(entities = [ImageData::class], version = 1)
@TypeConverters(ImageDataTypeConverter::class)
abstract class FavouriteDatabase: RoomDatabase() {
    abstract fun favouriteDao() : FavouriteDao
}