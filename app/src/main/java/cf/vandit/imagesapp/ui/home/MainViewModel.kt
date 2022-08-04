package cf.vandit.imagesapp.ui.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cf.vandit.imagesapp.data.models.ImageData
import cf.vandit.imagesapp.data.repositories.HomeRepoImpl
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {
    var TAG = "MainViewModel"

//    val database = Room.databaseBuilder(context, FavouriteDatabase::class.java, "favouriteDB").build()
//    private val repo: HomeRepoImpl = HomeRepoImpl(database.favouriteDao())
    private val repo: HomeRepoImpl = HomeRepoImpl()
    var images = MutableLiveData<List<ImageData>>()

    fun fetchImages(page: Int){
        Log.d(TAG, "fetching images... $page")
        viewModelScope.launch {
            val temp = repo.fetchImages(page)
            if(temp.isSuccess) {
                images.postValue(temp.getOrNull())
            }
        }
    }
}