package cf.vandit.imagesapp.network

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson

@Entity
data class ImageData(
    val alt_description: String?,
    val blur_hash: String?,
//    val categories: List<Any>,
    val color: String?,
    val created_at: String?,
//    val current_user_collections: List<Any>,
    val description: String?,
    val height: Int?,
    @PrimaryKey
    val id: String,
    var liked_by_user: Boolean? = false,
    val likes: Int?,
//    val links: Links,
    val promoted_at: String?,
//    val sponsorship: Sponsorship,
//    val topic_submissions: TopicSubmissions,
    val updated_at: String?,
    val urls: Urls,
    val user: User,
    val width: Int?
)

class ImageDataTypeConverter{
    @TypeConverter
    fun urlsToString(urls: Urls):String{
        return Gson().toJson(urls).toString()
    }

    @TypeConverter
    fun stringToUrls(string:String):Urls{
        return Gson().fromJson(string,Urls::class.java)
    }

    @TypeConverter
    fun userToString(user:User):String{
        return Gson().toJson(user).toString()
    }

    @TypeConverter
    fun stringToUser(string:String):User{
        return Gson().fromJson(string,User::class.java)
    }
}