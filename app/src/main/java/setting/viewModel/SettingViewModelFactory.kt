package setting.viewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import home.viewModel.HomeViewModel
import model.IWeatherRepository
import model.SettingsManager
import java.lang.IllegalArgumentException

class SettingViewModelFactory (private  val application: Application) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(SettingViewModel::class.java)){
            SettingViewModel(application) as T
        }else{
            throw IllegalArgumentException("View model Class not found")
        }

    }
}