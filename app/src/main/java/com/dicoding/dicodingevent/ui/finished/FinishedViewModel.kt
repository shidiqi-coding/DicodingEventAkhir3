package com.dicoding.dicodingevent.ui.finished


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.dicodingevent.EventRepository
import com.dicoding.dicodingevent.data.response.EventResponse
import com.dicoding.dicodingevent.data.response.ListEventsItem

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FinishedViewModel(private val repository: EventRepository) : ViewModel() {

    private val _eventlist = MutableLiveData<List<ListEventsItem>>()
    val eventlist: LiveData<List<ListEventsItem>> = _eventlist

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    companion object {
        private const val TAG = "FinishedViewModel"
    }

    init {
        fetchEvents(query = null)
    }

    private fun fetchEvents(query: String?) {
        _loading.value = true
        _error.value = null

        val call = repository.getFinishedEventsCallback(query)
        call.enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse> , response: Response<EventResponse>) {
                if (response.isSuccessful) {
                    _eventlist.value = response.body()?.listEvents
                } else {
                    _error.value = "Failed to fetch events: ${response.message()}"
                    Log.e(TAG , "onFailure: ${response.message()}")
                }
                _loading.value = false
            }

            override fun onFailure(call: Call<EventResponse> , t: Throwable) {
                _error.value = "Network error: ${t.message}"
                Log.e(TAG , "onFailure: ${t.message}")
                _loading.value = false
            }
        })
    }

    fun fetchFinishedEvents() {
        fetchEvents(query = null)
    }


    fun searchEvents(query: String) {
        fetchEvents(query = query)
    }

}

