package cf.vandit.imagesapp.di

import android.content.Context
import androidx.room.Room
import cf.vandit.imagesapp.data.database.FavouriteDao
import cf.vandit.imagesapp.data.database.FavouriteDatabase
import cf.vandit.imagesapp.data.network.service.CommonNetworkService
import cf.vandit.imagesapp.data.network.service.RetrofitService
import cf.vandit.imagesapp.data.repositories.HomeRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideCommonNetworkService(): CommonNetworkService {
        return RetrofitService.getRetroInstance().create(CommonNetworkService::class.java)
    }

    @Provides
    @Singleton
    fun provideHomeRepository(service: CommonNetworkService): HomeRepo {
        return HomeRepo(service)
    }

    @Provides
    @Singleton
    fun provideFavDao(favouriteDatabase: FavouriteDatabase): FavouriteDao {
        return favouriteDatabase.favouriteDao()
    }

    @Provides
    @Singleton
    fun provideFavDatabase(@ApplicationContext context: Context): FavouriteDatabase {
        synchronized(this){
            return Room.databaseBuilder(context, FavouriteDatabase::class.java, "favouriteDB").build()
        }
    }
}