package cf.vandit.imagesapp.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favourite")
data class Favourite(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val image: String?,
    val title: String?,
    val location: String?,
    val desc: String?,
    val isFav: Boolean?
)