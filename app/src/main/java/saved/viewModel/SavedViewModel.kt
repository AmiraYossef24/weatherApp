package saved.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.common.config.GservicesValue.value
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import model.IWeatherRepository
import model.Location
import model.WeatherResponse

class SavedViewModel (private  val _irepo : IWeatherRepository) : ViewModel() {

    private var _locationList = MutableSharedFlow<List<Location>>()
    val locationList : SharedFlow<List<Location>> = _locationList

    private var _weather = MutableLiveData<WeatherResponse>()
    val weather : LiveData<WeatherResponse> = _weather


    init {
        getAllSavedLocations()
    }

    fun getAllSavedLocations(){
        viewModelScope.launch(Dispatchers.IO){
            _irepo.getAllSavedLocation().collect(){
                _locationList.emit(it)
            }
        }
    }

    fun delete(location : Location){
        viewModelScope.launch(Dispatchers.IO){
            _irepo.deleteLocation(location)
            Log.i("TAG", "delete: ${location}")
        }
    }

    fun getCurrentWeather(lan : Double, lat : Double, apiKey :String){
        viewModelScope.launch{

            _weather.postValue(_irepo.getCurrentWeather(lan , lat , apiKey))
        }
    }

}