package cf.vandit.imagesapp
import cf.vandit.imagesapp.network.ImageData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {
    @GET("/photos")
    fun getImages(@Query("client_id") clientId: String = Constants.clientId): Call<List<ImageData>>
}