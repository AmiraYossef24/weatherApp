package home.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import model.DetailsResponse
import model.IWeatherRepository
import model.Location
import model.WeatherData
import model.WeatherResponse
import retrofit2.Response
import utility.ApiState

class HomeViewModel (private  val _irepo : IWeatherRepository) : ViewModel() {
//    private var _weather : MutableLiveData<WeatherResponse> = MutableLiveData<WeatherResponse>()
//    val weather : LiveData<WeatherResponse> = _weather

    private var _weather : MutableStateFlow<ApiState> = MutableStateFlow<ApiState>(ApiState.Loading)
    val weather : StateFlow<ApiState> = _weather

    private var _details : MutableLiveData<DetailsResponse> = MutableLiveData<DetailsResponse>()
    val details : LiveData<DetailsResponse> = _details

    private var _fiveDays : MutableLiveData<List<WeatherData>> = MutableLiveData<List<WeatherData>>()
    val fiveDays : LiveData<List<WeatherData>> = _fiveDays

    fun getCurrentWeather( lat : Double, lan : Double, apiKey :String ,  temp : String , lang : String  )= viewModelScope.launch{
        viewModelScope.launch(Dispatchers.IO){
            try {
                _irepo.getCurrentWeather(lat,lan,apiKey,temp,lang)
                    .collect {
                        _weather.value = ApiState.Success(it)
                    }
            }catch (e : Throwable){
                _weather.value=ApiState.Failure(e)
            }
        }
    }
    fun getWeatherDetails(lat : Double, lan : Double, apiKey :String ,  temp : String , lang : String  )=viewModelScope.launch{
        viewModelScope.launch(Dispatchers.IO) {

            _details.postValue(_irepo.getWeatherDetails(lat,lan,apiKey,temp,lang))
        }
    }

    fun getFiveDays(lat : Double, lan : Double, apiKey :String ,  temp : String , lang : String  ){
        viewModelScope.launch {
            _fiveDays.postValue(_irepo.getWeatherForecast(lat,lan,apiKey,temp,lang))
        }
    }

}