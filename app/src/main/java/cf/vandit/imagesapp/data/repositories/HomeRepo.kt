package cf.vandit.imagesapp.data.repositories

import android.util.Log
import cf.vandit.imagesapp.data.models.ImageData
import cf.vandit.imagesapp.data.network.service.CommonNetworkService
import cf.vandit.imagesapp.utils.Constants
import java.net.UnknownHostException
import javax.inject.Inject

private const val TAG = "HomeRepoImpl"

class HomeRepo @Inject constructor(private val service: CommonNetworkService) {

    suspend fun fetchImages(currentPage: Int): Result<List<ImageData>>{
        return try {
            val response = service.getImages(Constants.clientId, currentPage)
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Log.d(TAG, response.message())
                Result.failure(throw Exception(response.errorBody().toString()))
            }
        } catch (e: UnknownHostException) {
            Log.e(TAG, e.stackTraceToString())
            Result.failure(e)
        } catch (e: Exception){
            Log.e(TAG, e.stackTraceToString())
            Result.failure(e)
        }
    }
}