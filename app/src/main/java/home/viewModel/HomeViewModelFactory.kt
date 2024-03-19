package home.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import home.view.HomeFragment
import model.IWeatherRepository
import java.lang.IllegalArgumentException

class HomeViewModelFactory (private  val _irepo : IWeatherRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(HomeViewModel::class.java)){
            HomeViewModel(_irepo) as T
        }else{
            throw IllegalArgumentException("View model Class not found")
        }

    }
}