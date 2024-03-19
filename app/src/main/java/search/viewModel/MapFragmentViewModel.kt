package search.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import model.IWeatherRepository
import model.Location

class MapFragmentViewModel (private  val _irepo : IWeatherRepository) : ViewModel() {

    fun insert(location : Location){
        viewModelScope.launch(Dispatchers.IO){
            _irepo.insertLocation(location)
            Log.i("TAG", "insert: ${location}")
        }
    }


}