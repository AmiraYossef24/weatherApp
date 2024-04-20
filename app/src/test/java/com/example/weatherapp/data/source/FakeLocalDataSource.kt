package com.example.weatherapp.data.source

import AppDB.IWeatherLocalDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import model.Calender
import model.Location

class FakeLocalDataSource :
    IWeatherLocalDataSource {
    private val myList= mutableListOf<Location>()
    private val myClendarList= mutableListOf<Calender>()


    override suspend fun insertLocation(location: Location) {
        myList?.add(location)
    }

    override suspend fun deleteLocation(location: Location) {
        myList?.remove(location)
    }

    override suspend fun getAllSavedLocation(): Flow<List<Location>> {
        return flow {
            myList?.let {
                emit(it)
            }
        }
    }

    override suspend fun getAllCalender(): Flow<List<Calender>> {
        return flow {
            myClendarList?.let {
                emit(it)
            }
        }
    }

    override suspend fun insertCalender(calender: Calender) {
        myClendarList?.add(calender)
    }

    override suspend fun deleteCalender(calender: Calender) {
        myClendarList?.remove(calender)
    }
}
