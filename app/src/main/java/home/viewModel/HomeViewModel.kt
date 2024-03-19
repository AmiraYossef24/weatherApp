package home.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import model.DetailsResponse
import model.IWeatherRepository
import model.Location
import model.WeatherData
import model.WeatherResponse
import retrofit2.Response

class HomeViewModel (private  val _irepo : IWeatherRepository) : ViewModel() {
    private var _weather : MutableLiveData<WeatherResponse> = MutableLiveData<WeatherResponse>()
    val weather : LiveData<WeatherResponse> = _weather

    private var _details : MutableLiveData<DetailsResponse> = MutableLiveData<DetailsResponse>()
    val details : LiveData<DetailsResponse> = _details

    private var _fiveDays : MutableLiveData<List<WeatherData>> = MutableLiveData<List<WeatherData>>()
    val fiveDays : LiveData<List<WeatherData>> = _fiveDays

    fun getCurrentWeather(lan : Double, lat : Double, apiKey :String){
        viewModelScope.launch{

            _weather.postValue(_irepo.getCurrentWeather(lan , lat , apiKey))
        }
    }
    fun getWeatherDetails(lan : Double, lat : Double, apiKey :String){
        viewModelScope.launch {
            _details.postValue(_irepo.getWeatherDetails(lan,lat,apiKey))
        }
    }

    fun getFiveDays(lan : Double, lat : Double, apiKey :String){
        viewModelScope.launch {
            _fiveDays.postValue(_irepo.getWeatherForecast(lan,lat,apiKey))
        }
    }

}