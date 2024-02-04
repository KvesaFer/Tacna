package com.mkves.tacna.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mkves.tacna.db.EventDao
import com.mkves.tacna.db.UserFundsDao
import com.mkves.tacna.model.Event
import com.mkves.tacna.model.UserFunds
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EventViewModel(private val eventDao: EventDao, private val userFundsDao: UserFundsDao) : ViewModel() {
    val eventIdLiveData = MutableLiveData<Int>()

    fun createOrUpdateEvent(event: Event, myDeposit: Double, givenDeposit: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            val existingEvent = eventDao.getEventByName(event.eventName)
            var eventId = -1
            if (existingEvent != null) {
                existingEvent.beerPrice = event.beerPrice
                existingEvent.juicePrice = event.juicePrice
                existingEvent.shotPrice = event.shotPrice
                existingEvent.payoutPercentage = event.payoutPercentage
                eventDao.updateEvent(existingEvent)

                eventId = existingEvent.id
                val userFunds = UserFunds(eventId, myDeposit, givenDeposit)
                userFundsDao.insertOrUpdate(userFunds)
            } else {
                eventId = eventDao.insertOrUpdateEvent(event).toInt()
                val userFunds = UserFunds(eventId, myDeposit, givenDeposit)
                userFundsDao.insertOrUpdate(userFunds)
            }

            withContext(Dispatchers.Main) {
                eventIdLiveData.value = eventId
            }
        }
    }

    fun updateEvent(event: Event) = viewModelScope.launch(Dispatchers.IO) {
        eventDao.updateEvent(event)
    }

    fun getEventById(eventId: Int, onEventFetched: (Event) -> Unit) {
        viewModelScope.launch {
            val event = withContext(Dispatchers.IO) {
                eventDao.getEventById(eventId)
            }
            onEventFetched(event)
        }
    }

    fun getUserFundsByEventId(eventId: Int, onUserFundsFetched: (UserFunds) -> Unit) {
        viewModelScope.launch {
            val userFunds = withContext(Dispatchers.IO) {
                userFundsDao.getUserFundsByEventId(eventId)
            }
            onUserFundsFetched(userFunds)
        }
    }
}


