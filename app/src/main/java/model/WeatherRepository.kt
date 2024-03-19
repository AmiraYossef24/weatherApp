package model

import AppDB.WeatherLocalDataSource
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import kotlinx.coroutines.flow.Flow
import network.weatherRemoteDataSource
import retrofit2.Response
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class WeatherRepository private constructor(

    private var _weatherRemoteDataSource: weatherRemoteDataSource,
    private var _weatherLocalDataSource: WeatherLocalDataSource,

)


    : IWeatherRepository {


    companion object{
        @Volatile
        private var instance: WeatherRepository? = null
        fun getInstance(
            remoteDataSource: weatherRemoteDataSource,
            localDataSource: WeatherLocalDataSource
        ): WeatherRepository {
            return instance ?: synchronized(this) {
                instance ?: WeatherRepository(remoteDataSource,localDataSource).also { instance = it }
            }
        }

    }

    override suspend fun getCurrentWeather(lan : Double, lat : Double, apiKey :String ): WeatherResponse? {
        val response = _weatherRemoteDataSource.getWeatherOverNetwork(lan, lat,apiKey)
        return if (response.isSuccessful) {
            // Return the response body
            Log.i(
                "TAG",
                "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<getWeatherDetails: ${response.body()}>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"
            )
            response.body()
        } else {
            Log.i("TAG", response.errorBody()?.string() ?: "Unknown error")
            null // Return null in case of error
        }
    }


    override suspend fun getWeatherDetails(lan : Double, lat : Double, apiKey :String ): DetailsResponse? {
        val response = _weatherRemoteDataSource.getDetailsOverNetwork(lan, lat,apiKey)
        return if (response.isSuccessful) {
            Log.i(
                "TAG",
                "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<getWeatherDetails: ${response.body()}>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"
            )
            response.body()
        } else {
            Log.i("TAG", response.errorBody()?.string() ?: "Unknown error")
            null // Return null in case of error
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getWeatherForecast(lan: Double, lat: Double, apiKey: String): List<WeatherData> {
        val response = _weatherRemoteDataSource.getDetailsOverNetwork(lan, lat, apiKey)
        return response.body()?.let { filterTemperaturesPerDay(it) } ?: emptyList()
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun filterTemperaturesPerDay(response: DetailsResponse): List<WeatherData> {
        val filteredList = mutableListOf<WeatherData>()

        // Iterate through each day's data
        var previousDate = LocalDate.MIN
        for (weatherData in response.list) {
            val currentDate = LocalDate.parse(weatherData.dt_txt.substring(0, 10))

            // Check if it's a new day
            if (currentDate != previousDate) {
                // Add the first temperature data for the new day
                filteredList.add(weatherData)
                previousDate = currentDate
            }
        }

        return filteredList
    }

    override suspend  fun getAllSavedLocation() : Flow<List<Location>>{

        var response=_weatherLocalDataSource.getAllSavedLocation()
        return response
    }

    override suspend fun insertLocation(location : Location){
        _weatherLocalDataSource.insertLocation(location)
    }

    override suspend fun deleteLocation(location: Location){
        _weatherLocalDataSource.deleteLocation(location)
    }


}