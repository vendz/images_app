package cf.vandit.imagesapp.data.repositories

import android.util.Log
import cf.vandit.imagesapp.data.database.FavouriteDao
import cf.vandit.imagesapp.data.models.ImageData
import cf.vandit.imagesapp.data.network.service.RetrofitService
import cf.vandit.imagesapp.utils.Constants
import java.net.UnknownHostException

private const val TAG = "HomeRepoImpl"

//class HomeRepoImpl(private val favouriteDao: FavouriteDao) {
class HomeRepoImpl {
//    companion object {
//        private val ourInstance: HomeRepoImpl = HomeRepoImpl()
//
//        fun getInstanceRepo(): HomeRepoImpl {
//            return ourInstance
//        }
//    }

    private val service = RetrofitService.getRetroInstance().create(HomeRepo::class.java)

    suspend fun fetchImages(currentPage: Int): Result<List<ImageData>>{
        try {
            val response = service.getImages(Constants.clientId, currentPage)
            return if (response.isSuccessful) {
                Result.success(response.body()!!)
        //                for(res in response.body()!!){
        //                    Log.d(TAG, "fetchImages: ${res.id}")
        //                    if(favouriteDao.getImage(res.id) != null){
        //                        res.liked_by_user = true
        //                    }
        //                }
        //                return Result.success(response.body()!!)
            } else {
                Log.d(TAG, response.message())
                Result.failure(throw Exception(response.errorBody().toString()))
            }
        } catch (e: UnknownHostException) {
            Log.e(TAG, e.stackTraceToString())
            return Result.failure(e)
        } catch (e: Exception){
            Log.e(TAG, e.stackTraceToString())
            return Result.failure(e)
        }
    }
}