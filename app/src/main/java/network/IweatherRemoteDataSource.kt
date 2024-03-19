package network

import model.DetailsResponse
import model.WeatherData
import model.WeatherResponse
import retrofit2.Response

interface IweatherRemoteDataSource {
    suspend fun getWeatherOverNetwork(lat: Double,
                                      lon: Double,
                                      apiKey: String
    ) : Response<WeatherResponse>

    suspend fun getDetailsOverNetwork(lat: Double,
                                      lon: Double ,
                                      apiKey: String
    ) : Response<DetailsResponse>

}