package network

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import model.City
import model.Clouds
import model.Coord
import model.Coord2
import model.DetailsResponse
import model.Main
import model.SettingsManager
import model.Sys
import model.WeatherData
import model.WeatherResponse
import model.Wind
import retrofit2.Response
import java.time.LocalDate


class weatherRemoteDataSource private constructor() : IweatherRemoteDataSource {

    private val weatherService : ApiService by lazy {
       RetrofitHelper.retrofitInstace.create(ApiService::class.java)

    }

    companion object{
        private var instance : weatherRemoteDataSource?=null

        @Synchronized
        fun getInstance() : weatherRemoteDataSource{
            return instance ?: synchronized(this){
                val temp = weatherRemoteDataSource()
                instance=temp
                temp
            }
        }
    }

    override suspend fun getWeatherOverNetwork(
        lat: Double,
        lan: Double,
        apiKey: String,
        temp: String,
        lang: String
    ): StateFlow<WeatherResponse> {
        val weatherStateFlow = MutableStateFlow(WeatherResponse(Coord(0.0, 0.0),
            listOf(),
            "",
            Main(0.0, 0.0, 0.0, 0.0, 0, 0),
            0,
            Wind(0.0, 0),
            Clouds(0),
            0,
            Sys(0, 0, "", 0, 0),
            0,
            0,
            "",
            0
        ))
        try {
            val response = weatherService.getWeatherData(lat, lan, apiKey, temp, lang).body()
            response?.let {
                weatherStateFlow.value = it // Update with the response if it's not null
            }
        } catch (e: Exception) {
            // Handle exceptions if needed
        }
        return weatherStateFlow
    }


    override suspend fun getDetailsOverNetwork(lat : Double, lan : Double,  apiKey :String, temp : String , lang : String) : Response<DetailsResponse> {
        val response = weatherService.getAllDetails(lat, lan,apiKey,temp,lang)
        if(response.isSuccessful){
            Response.success(response.body()) // Wrap the response body in Response.success()
            Log.i("TAG", "getAllProducts Repository: The Products = ${response.toString()}")
        }
        else{
            Log.i("TAG", response.errorBody().toString())
        }
        return response
    }



    /////////
//        val response = weatherService.getWeatherData(lat, lan,apiKey,temp,lang)
//        if(response.isSuccessful){
//            Response.success(response.body()) // Wrap the response body in Response.success()
//            Log.i("TAG", "getAllProducts Repository: The Products = ${response.toString()}")
//        }
//        else{
//            Log.i("TAG", "error")
//        }
//        return response
//
//    override suspend fun getDetailsOverNetwork(lat : Double, lan : Double,  apiKey :String, temp : String , lang : String) : Response<DetailsResponse> {
//        val response = weatherService.getAllDetails(lat, lan,apiKey,temp,lang)
//        if(response.isSuccessful){
//            Response.success(response.body()) // Wrap the response body in Response.success()
//            Log.i("TAG", "getAllProducts Repository: The Products = ${response.toString()}")
//        }
//        else{
//            Log.i("TAG", response.errorBody().toString())
//        }
//        return response
//    }



}