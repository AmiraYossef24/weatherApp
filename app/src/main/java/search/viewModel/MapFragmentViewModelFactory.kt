package search.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import model.IWeatherRepository
import java.lang.IllegalArgumentException

class MapFragmentViewModelFactory (private  val _irepo : IWeatherRepository) : ViewModelProvider.Factory {


    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(MapFragmentViewModel::class.java)){
            MapFragmentViewModel(_irepo) as T
        }else{
            throw IllegalArgumentException("View model Class not found")
        }

    }
}