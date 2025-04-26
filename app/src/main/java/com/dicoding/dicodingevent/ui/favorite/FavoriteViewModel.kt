package com.dicoding.dicodingevent.ui.favorite

import androidx.lifecycle.ViewModel
import com.dicoding.dicodingevent.EventRepository


class FavoriteViewModel(private val eventRepository: EventRepository) : ViewModel() {

    fun getFavoritedEvents() = eventRepository.getAllFavorites()


}
