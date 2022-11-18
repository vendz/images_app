package cf.vandit.imagesapp.data.network.service
import cf.vandit.imagesapp.utils.Constants
import com.localebro.okhttpprofiler.OkHttpProfilerInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitService {
    companion object{
        fun getRetroInstance(): Retrofit {
            val client = OkHttpClient.Builder()
                .addInterceptor(OkHttpProfilerInterceptor())
                .build()

            return Retrofit.Builder()
                .baseUrl(Constants.baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        }
    }
}