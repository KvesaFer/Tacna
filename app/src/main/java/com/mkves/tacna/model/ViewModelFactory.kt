package com.mkves.tacna.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mkves.tacna.db.EventDao
import com.mkves.tacna.db.UserFundsDao
import com.mkves.tacna.viewmodel.EventViewModel

class EventViewModelFactory(
    private val eventDao: EventDao,
    private val userFundsDao: UserFundsDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EventViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return EventViewModel(eventDao, userFundsDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}