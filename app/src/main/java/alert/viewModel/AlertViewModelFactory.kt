package alert.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import model.IWeatherRepository
import saved.viewModel.SavedViewModel
import java.lang.IllegalArgumentException

class AlertViewModelFactory (private  val _irepo : IWeatherRepository) : ViewModelProvider.Factory {


    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(AlertViewModel::class.java)){
            AlertViewModel(_irepo) as T
        }else{
            throw IllegalArgumentException("View model Class not found")
        }

    }
}