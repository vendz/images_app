package cf.vandit.imagesapp.ui.favourites

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cf.vandit.imagesapp.data.database.FavouriteDao
import cf.vandit.imagesapp.data.models.ImageData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavouritesViewModel @Inject constructor(private val favouriteDao: FavouriteDao): ViewModel() {
    private val TAG = "FavouritesViewModel"

    val result: LiveData<List<ImageData>> = favouriteDao.getAllImages()

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
}