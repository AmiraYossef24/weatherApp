package model

import android.os.Build
import androidx.annotation.RequiresApi
import kotlinx.coroutines.flow.StateFlow
import retrofit2.Response
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter


data class WeatherResponse(
    val coord: Coord,
    val weather: List<Weather>,
    val base: String,
    val main: Main,
    val visibility: Int,
    val wind: Wind,
    val clouds: Clouds,
    val dt: Long,
    val sys: Sys,
    val timezone: Int,
    val id: Int,
    val name: String,
    val cod: Int
)  {
    fun getTemperatureInCelsius(): String {
        val temperatureCelsius = main.temp - 273.15
        return String.format("%.1f°C", temperatureCelsius)
    }

    fun getTempFeelsLikeInCelsius(): Int {
        val temperatureCelsius = main.feels_like - 273.15
        return temperatureCelsius.toInt()
    }

    fun getUV () : Int {
        val Uv = sys.type
        return Uv
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getDayOfWeek(): String {
        val dateTime = Instant.ofEpochSecond(dt)
        val localDateTime = LocalDateTime.ofInstant(dateTime, ZoneId.systemDefault())
        val dayOfWeek = localDateTime.dayOfWeek
        return dayOfWeek.name
    }
}

data class Coord(
    val lon: Double,
    val lat: Double
)

data class Weather(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)

data class Main(
    val temp: Double,
    val feels_like: Double,
    val temp_min: Double,
    val temp_max: Double,
    val pressure: Int,
    val humidity: Int
)

data class Wind(
    val speed: Double,
    val deg: Int
)

data class Clouds(
    val all: Int
)

data class Sys(
    val type: Int,
    val id: Int,
    val country: String,
    val sunrise: Long,
    val sunset: Long)