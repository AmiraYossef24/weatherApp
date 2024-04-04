package model

import AppDB.IWeatherLocalDataSource
import AppDB.WeatherLocalDataSource
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import network.IweatherRemoteDataSource
import network.RetrofitHelper
import network.weatherRemoteDataSource
import retrofit2.Response
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class WeatherRepository private constructor(

    private var _weatherRemoteDataSource: IweatherRemoteDataSource,
    private var _weatherLocalDataSource: IWeatherLocalDataSource,

    )


    : IWeatherRepository {


    companion object{
        @Volatile
        private var instance: WeatherRepository? = null
        fun getInstance(
            remoteDataSource: IweatherRemoteDataSource,
            localDataSource: IWeatherLocalDataSource
        ): WeatherRepository {
            return instance ?: synchronized(this) {
                instance ?: WeatherRepository(remoteDataSource,localDataSource).also { instance = it }
            }
        }

    }
    override suspend fun getCurrentWeather( lat : Double,lan : Double , apiKey :String, temp : String , lang : String  ): WeatherResponse? {
        val response = _weatherRemoteDataSource.getWeatherOverNetwork( lat,lan,apiKey,temp,lang)
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

    override suspend fun getWeatherDetails( lat : Double, lan : Double, apiKey :String , temp : String, lang :String ): DetailsResponse? {
        val response = _weatherRemoteDataSource.getDetailsOverNetwork(lat, lan,apiKey , temp ,lang)
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
    override suspend fun getWeatherForecast( lat: Double, lan: Double, apiKey: String , temp :String, lang:String): List<WeatherData> {
        val response = _weatherRemoteDataSource.getDetailsOverNetwork( lat,lan, apiKey,temp,lang)
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

    override suspend fun getAllSavedCalender() : Flow<List<Calender>>{
        var res=_weatherLocalDataSource.getAllCalender()
        return res
    }

    override suspend fun insertCalender(calender: Calender){
        _weatherLocalDataSource.insertCalender(calender)
    }

    override suspend fun deleteCalender(calender: Calender){
        _weatherLocalDataSource.deleteCalender(calender)
    }





}