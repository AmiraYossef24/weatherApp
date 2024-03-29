package network

import kotlinx.coroutines.flow.StateFlow
import model.DetailsResponse
import model.SettingsManager
import model.WeatherData
import model.WeatherResponse
import retrofit2.Response

interface IweatherRemoteDataSource {
    suspend fun getWeatherOverNetwork( lat : Double, lan : Double,  apiKey :String, temp : String , lang : String) : Response<WeatherResponse>

    suspend fun getDetailsOverNetwork(lat : Double, lan : Double,  apiKey :String, temp : String , lang : String) : Response<DetailsResponse>


}