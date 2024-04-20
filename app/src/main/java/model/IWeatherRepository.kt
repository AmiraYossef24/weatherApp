package model

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import retrofit2.Response

interface IWeatherRepository {
    suspend fun getCurrentWeather( lat : Double,lan : Double , apiKey :String, temp : String , lang : String  ): StateFlow<WeatherResponse>
    suspend fun getWeatherDetails( lat : Double, lan : Double, apiKey :String , temp : String, lang :String ): DetailsResponse?
    suspend fun getWeatherForecast( lat: Double, lan: Double, apiKey: String , temp :String, lang:String): List<WeatherData>
    suspend  fun getAllSavedLocation() : Flow<List<Location>>
    suspend fun insertLocation(location : Location)
    suspend fun deleteLocation(location : Location)
    suspend fun getAllSavedCalender() : Flow<List<Calender>>
    suspend fun insertCalender(calender: Calender)
    suspend fun deleteCalender(calender: Calender)

}