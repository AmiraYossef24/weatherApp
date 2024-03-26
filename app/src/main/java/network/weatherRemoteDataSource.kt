package network

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import model.DetailsResponse
import model.SettingsManager
import model.WeatherData
import model.WeatherResponse
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

    override suspend fun getWeatherOverNetwork( lat : Double, lan : Double,  apiKey :String, temp : String , lang : String) : Response<WeatherResponse> {
        val response = weatherService.getWeatherData(lat, lan,apiKey,temp,lang)
        if(response.isSuccessful){
            Response.success(response.body()) // Wrap the response body in Response.success()
            Log.i("TAG", "getAllProducts Repository: The Products = ${response.toString()}")
        }
        else{
            Log.i("TAG", response.errorBody().toString())
        }
        return response
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



}