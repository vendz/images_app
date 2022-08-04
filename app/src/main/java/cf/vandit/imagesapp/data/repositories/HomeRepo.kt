package cf.vandit.imagesapp.data.repositories

import cf.vandit.imagesapp.data.models.ImageData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface HomeRepo {
    @GET("/photos")
    suspend fun getImages(@Query("client_id") clientId: String, @Query("page") page: Int): Response<List<ImageData>>
}