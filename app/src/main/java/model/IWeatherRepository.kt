package model

import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface IWeatherRepository {
    suspend fun getCurrentWeather(lan : Double, lat : Double, apiKey :String ): WeatherResponse?
    suspend fun getWeatherDetails(lan : Double, lat : Double, apiKey :String ): DetailsResponse?
    suspend fun getWeatherForecast(lan: Double, lat: Double, apiKey: String): List<WeatherData>
    suspend  fun getAllSavedLocation() : Flow<List<Location>>
    suspend fun insertLocation(location : Location)
    suspend fun deleteLocation(location : Location)
}