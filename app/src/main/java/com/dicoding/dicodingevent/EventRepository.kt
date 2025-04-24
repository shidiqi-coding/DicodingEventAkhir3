package com.dicoding.dicodingevent

import androidx.lifecycle.LiveData
import com.dicoding.dicodingevent.data.response.DetailEventResponse
import com.dicoding.dicodingevent.data.response.EventResponse
import com.dicoding.dicodingevent.data.retrofit.ApiService
import com.dicoding.dicodingevent.entity.FavoriteEvent
import com.dicoding.dicodingevent.entity.FavoriteEventDao
import retrofit2.Call


class EventRepository(private val apiService: ApiService,  private val favoriteEventDao: FavoriteEventDao) {
    fun getEventsCallback(active: Int , query: String?): Call<EventResponse> {
        return apiService.getEvent(active , query)
    }

    fun getAllFavorites(): LiveData<List<FavoriteEvent>> {
        return favoriteEventDao.getAllFavorites()
    }

//    fun getEventDetail(id: String): Call<DetailEventResponse> {
//        return apiService.getDetailEvents(id)
//    }

    companion object {
        @Volatile
        private var instance: EventRepository? = null

        fun getInstance(apiService: ApiService, favoriteEventDao: FavoriteEventDao): EventRepository {
            return instance ?: synchronized(this) {
                instance ?: EventRepository(apiService, favoriteEventDao).also { instance = it }
            }
        }
    }
}

