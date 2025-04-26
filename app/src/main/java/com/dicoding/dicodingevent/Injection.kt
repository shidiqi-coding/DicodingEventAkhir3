package com.dicoding.dicodingevent

import android.content.Context
import com.dicoding.dicodingevent.data.retrofit.ApiConfig
import com.dicoding.dicodingevent.entity.FavoriteDatabase


object Injection {
    fun provideRepository(context: Context): EventRepository {
        val apiService = ApiConfig.getApiService()
        val database = FavoriteDatabase.getDatabase(context)
        val dao = database.favoriteEventDao()
        return EventRepository.getInstance(apiService , dao)
    }
}
