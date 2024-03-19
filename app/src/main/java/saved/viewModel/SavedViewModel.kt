package saved.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import model.IWeatherRepository
import model.Location

class SavedViewModel (private  val _irepo : IWeatherRepository) : ViewModel() {

    private var _locationList = MutableLiveData<List<Location>>()
    val locationList : LiveData<List<Location>> = _locationList


    init {
        getAllSavedLocations()
    }

    fun getAllSavedLocations(){
        viewModelScope.launch(Dispatchers.IO){

            _irepo.getAllSavedLocation().collect(){
                _locationList.postValue(it)
            }

        }
    }

    fun delete(location : Location){
        viewModelScope.launch(Dispatchers.IO){
            _irepo.deleteLocation(location)
            Log.i("TAG", "delete: ${location}")
        }
    }


}