package cf.vandit.imagesapp.utils

import android.util.Log
import retrofit2.Response

suspend fun <T> getResult(call: suspend () -> Response<T>): Result<T> {
    try {
        val response = call()
        if (response.isSuccessful && response.body() != null)
            return Result.Success(response.body()!!)
        return Result.Error(Exception(response.message()))
    } catch (e: Exception) {
        Log.e("VANDIT", "getResult: ", e)
        return Result.Error(e)
    }
}