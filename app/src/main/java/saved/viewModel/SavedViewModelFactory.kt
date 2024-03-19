package saved.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import model.IWeatherRepository
import search.viewModel.MapFragmentViewModel
import java.lang.IllegalArgumentException

class SavedViewModelFactory(private  val _irepo : IWeatherRepository) : ViewModelProvider.Factory {


    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(SavedViewModel::class.java)){
            SavedViewModel(_irepo) as T
        }else{
            throw IllegalArgumentException("View model Class not found")
        }

    }
}