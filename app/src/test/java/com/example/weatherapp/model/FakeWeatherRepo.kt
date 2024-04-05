package com.example.weatherapp.model

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import model.Calender
import model.DetailsResponse
import model.IWeatherRepository
import model.Location
import model.WeatherData
import model.WeatherResponse

class FakeWeatherRepo(private val myList : MutableList<Location>?) : IWeatherRepository {

    override suspend fun getCurrentWeather(
        lat: Double,
        lan: Double,
        apiKey: String,
        temp: String,
        lang: String
    ): WeatherResponse? {
        TODO("Not yet implemented")
    }

    override suspend fun getWeatherDetails(
        lat: Double,
        lan: Double,
        apiKey: String,
        temp: String,
        lang: String
    ): DetailsResponse? {
        TODO("Not yet implemented")
    }

    override suspend fun getWeatherForecast(
        lat: Double,
        lan: Double,
        apiKey: String,
        temp: String,
        lang: String
    ): List<WeatherData> {
        TODO("Not yet implemented")
    }

    override suspend fun getAllSavedLocation(): Flow<List<Location>> {

        return flow {
            myList?.let {
                emit(it)
            }
        }
       // return  flowOf(myList?.toList() ?: emptyList())
    }

    override suspend fun insertLocation(location: Location) {

        myList?.add(location)
    }

    override suspend fun deleteLocation(location: Location) {

        myList?.remove(location)
    }

    override suspend fun getAllSavedCalender(): Flow<List<Calender>> {
        TODO("Not yet implemented")
    }

    override suspend fun insertCalender(calender: Calender) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteCalender(calender: Calender) {
        TODO("Not yet implemented")
    }
}