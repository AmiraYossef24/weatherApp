package alert.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import model.Calender
import model.IWeatherRepository
import model.Location
import model.WeatherResponse

class AlertViewModel (private  val _irepo : IWeatherRepository) : ViewModel() {

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
}