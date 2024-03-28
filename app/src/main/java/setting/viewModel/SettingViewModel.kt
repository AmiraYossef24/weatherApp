package setting.viewModel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import model.IWeatherRepository
import model.SettingsManager
import model.WeatherRepository

class SettingViewModel (private val application: Application) : ViewModel() {

    private var settingsSharedPreference = application.getSharedPreferences("Setting", Context.MODE_PRIVATE)
    private val _settingChanges = MutableSharedFlow<SettingsManager>()
    val settingChanges: SharedFlow<SettingsManager> = _settingChanges

    //to put in SP
    fun updateSettings(settings: SettingsManager) {

        with(settingsSharedPreference.edit()){
            putString("selectedLang" , settings.lang)
            putString("selectedTemp",settings.tempe)
            putString("selectedLocation",settings.loc)
            putString("selectedWindSpeed",settings.wind)
            apply()
        }
    }

    fun emitChangingSetting () {
        val updatedSetting = getSettingDataFromSP()
        Log.i("Init", "inInit Scope: $updatedSetting ")
        viewModelScope.launch {
            _settingChanges.emit(updatedSetting)
        }
    }
    //to get from SP
    fun getSettingDataFromSP () : SettingsManager{
        return SettingsManager(
            settingsSharedPreference.getString("selectedLang","") ?: "",
            settingsSharedPreference.getString("selectedTemp","") ?: "Meter/Sec",
            settingsSharedPreference.getString("selectedLocation","") ?: "",
            settingsSharedPreference.getString("selectedWindSpeed","") ?: ""
        )
    }


}