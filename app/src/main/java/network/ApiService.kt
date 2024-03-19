package network

import model.DetailsResponse
import model.WeatherResponse
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit



interface ApiService {

    @GET("weather")
    suspend fun getWeatherData(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") apiKey: String
    ): Response<WeatherResponse>

    @GET("forecast")
    suspend fun getAllDetails(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") apiKey: String
    ): Response<DetailsResponse>
}

    object RetrofitHelper {

        private const val BASE_URL = "https://api.openweathermap.org/data/2.5/"


        private val client = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS) // Increase the connect timeout
            .readTimeout(30, TimeUnit.SECONDS) // Increase the read timeout
            .writeTimeout(30, TimeUnit.SECONDS) // Increase the write timeout
            .build()

        val retrofitInstace = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofitInstace.create(ApiService::class.java)
    }
