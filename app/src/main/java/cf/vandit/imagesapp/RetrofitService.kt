package cf.vandit.imagesapp
import cf.vandit.imagesapp.network.ImageData
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitService {
//    @GET("/photos")
//    fun getImages1(@Query("client_id") clientId: String = Constants.clientId): Call<List<ImageData>>

    @GET("/photos")
    suspend fun getImages(@Query("client_id") clientId: String, @Query("page") page: Int): Response<List<ImageData>>

    companion object{
        fun create(): RetrofitService {
            return Retrofit.Builder()
                .baseUrl(Constants.baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(RetrofitService::class.java)
        }
    }
}