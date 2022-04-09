package cf.vandit.imagesapp
import cf.vandit.imagesapp.network.ImageData
import com.localebro.okhttpprofiler.OkHttpProfilerInterceptor
import okhttp3.OkHttpClient
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
//            val logger = HttpLoggingInterceptor()
//            logger.level = HttpLoggingInterceptor.Level.BASIC

            val client = OkHttpClient.Builder()
                .addInterceptor(OkHttpProfilerInterceptor())
                .build()

            return Retrofit.Builder()
                .baseUrl(Constants.baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
                .create(RetrofitService::class.java)
        }
    }
}