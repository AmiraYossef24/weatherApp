package alert.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import model.Calender
import model.Clouds
import model.Coord
import model.IWeatherRepository
import model.Location
import model.Main
import model.Sys
import model.WeatherResponse
import model.Wind
import utility.ApiState

class AlertViewModel (private  val _irepo : IWeatherRepository) : ViewModel() {

    val initialWeather = WeatherResponse(
        coord = Coord(0.0, 0.0),
        weather = listOf(),
        base = "",
        main = Main(0.0, 0.0, 0.0, 0.0, 0, 0),
        visibility = 0,
        wind = Wind(0.0, 0),
        clouds = Clouds(0),
        dt = 0L,
        sys = Sys(0, 0, "",0,0),
        timezone = 0,
        id = 0,
        name = "",
        cod = 0
    )

    private var _weather : MutableStateFlow<WeatherResponse> = MutableStateFlow<WeatherResponse>(initialWeather)
    val weather : StateFlow<WeatherResponse> = _weather

    private var _calenderList = MutableSharedFlow<List<Calender>>()
    val calenderList : SharedFlow<List<Calender>> = _calenderList



    init {
        getAllSavedCalender()
    }

    fun getAllSavedCalender(){
        viewModelScope.launch(Dispatchers.IO){
            _irepo.getAllSavedCalender().collect(){
                _calenderList.emit(it)
            }
        }
    }

    fun delete(calender: Calender){
        viewModelScope.launch(Dispatchers.IO){
            _irepo.deleteCalender(calender)
            Log.i("TAG", "delete: ${calender}")
        }
    }


    fun insert(calender: Calender){
        viewModelScope.launch(Dispatchers.IO){
            _irepo.insertCalender(calender)
        }
    }

    fun getAlertDescription( lat : Double, lan : Double, apiKey :String ,  temp : String , lang : String  )= viewModelScope.launch{
        viewModelScope.launch(Dispatchers.IO){
            try {
                _irepo.getCurrentWeather(lat,lan,apiKey,temp,lang)
                    .collect {
                        _weather.value = it
                    }
            }catch (e : Throwable){
            }
        }
    }
}