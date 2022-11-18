package cf.vandit.imagesapp.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cf.vandit.imagesapp.data.database.FavouriteDao
import cf.vandit.imagesapp.data.models.ImageData
import cf.vandit.imagesapp.data.repositories.HomeRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repo: HomeRepo,
    private val favouriteDao: FavouriteDao,
) : ViewModel() {
    private var TAG = "MainViewModel"

    private val _images: MutableLiveData<List<ImageData>> = MutableLiveData()
    val images: LiveData<List<ImageData>>
        get() = _images

    fun fetchImages(page: Int){
        Log.d(TAG, "fetching images... $page")
        viewModelScope.launch {
            val temp = repo.fetchImages(page)
            if(temp.isSuccess) {
                for(item in temp.getOrNull()!!){
                    item.liked_by_user = isImageLiked(item)
                }
                _images.postValue(temp.getOrNull())
            }
        }
    }

    fun insertImage(item: ImageData){
        viewModelScope.launch {
            item.liked_by_user = true
            favouriteDao.insertImage(item)
        }
    }

    fun deleteImage(item: ImageData){
        viewModelScope.launch {
            item.liked_by_user = false
            favouriteDao.deleteImage(item)
        }
    }

    suspend fun isImageLiked(item: ImageData): Boolean {
        return favouriteDao.getImage(item.id) != null
    }
}