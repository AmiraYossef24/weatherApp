package setting.view

import AppDB.WeatherLocalDataSource
import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.airbnb.lottie.LottieAnimationView
import com.example.weatherapp.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.material.navigation.NavigationView
import model.SettingsManager
import model.WeatherRepository
import network.weatherRemoteDataSource
import saved.viewModel.SavedViewModel
import saved.viewModel.SavedViewModelFactory
import setting.viewModel.SettingViewModel
import setting.viewModel.SettingViewModelFactory
import home.view.HomeFragment
import java.util.Locale

class SettingFragment : Fragment() {
    lateinit var updatedSettings : SettingsManager
    lateinit var arabic: RadioButton
    lateinit var english : RadioButton
    lateinit var kelvin: RadioButton
    lateinit var cel: RadioButton
    lateinit var far: RadioButton
    lateinit var map : RadioButton
    lateinit var settingViewModel: SettingViewModel
    lateinit var settingFactory: SettingViewModelFactory
    lateinit var starsViewAnim  : LottieAnimationView
    lateinit var myNavigationView : NavigationView
    private var isLanguageChanged = false
    lateinit var langRadioGroup: RadioGroup

    lateinit var animationView: LottieAnimationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting, container, false)
    }

    override fun onResume() {
        super.onResume()
        updatedSettings  = settingViewModel.getSettingDataFromSP()

    }
//
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    animationView=view.findViewById(R.id.settingAnimationView)
    myNavigationView = view.findViewById(R.id.navigationViewSettingId)
    starsViewAnim=view.findViewById(R.id.starSettingAnimationView)
    starsViewAnim.setAnimation(R.raw.stars)
    starsViewAnim.playAnimation()

    langRadioGroup=view.findViewById(R.id.langRadioGroup)
    arabic=view.findViewById(R.id.arabic_radio_id)
    english=view.findViewById(R.id.english_radio_id)
    kelvin=view.findViewById(R.id.kel_radio_id)
    cel=view.findViewById(R.id.cel_radio_id)
    far=view.findViewById(R.id.far_radio_id)
    map=view.findViewById(R.id.map_radio_id)
    settingFactory = SettingViewModelFactory(requireActivity().application)
    settingViewModel = ViewModelProvider(this, settingFactory).get(SettingViewModel::class.java)

    animationView.setAnimation(R.raw.snow_anim)
    animationView.playAnimation()

    arabic.setOnClickListener {
        updatedSettings.lang="ar"
        settingViewModel.updateSettings(updatedSettings)

        setAppLanguage(requireContext(),"ar")

    }
    english.setOnClickListener {
        updatedSettings.lang="en"
        settingViewModel.updateSettings(updatedSettings)
        setAppLanguage(requireContext(),"en")

    }
        kelvin.setOnClickListener {
            updatedSettings.tempe="metric"
            settingViewModel.updateSettings(updatedSettings)
            kelvin.isChecked=true
        }
        cel.setOnClickListener {
            updatedSettings.tempe="standard"
            settingViewModel.updateSettings(updatedSettings)
            cel.isChecked=true
        }

        far.setOnClickListener {
            updatedSettings.tempe="imperial"
            settingViewModel.updateSettings(updatedSettings)
            far.isChecked=true
        }

        map.setOnClickListener {
            val navController =
                Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
            navController.navigate(R.id.mapFragmentThree)

        }

        myNavigationView.setNavigationItemSelectedListener { menuItem ->
            if (menuItem.itemId == R.id.nav_home) {
                Toast.makeText(requireContext(),"home clicked ", Toast.LENGTH_LONG).show()
                val navController =
                    Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                navController.navigate(R.id.homeFragment)
            }
            if (menuItem.itemId == R.id.nav_save) {
                val navController =
                    Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                navController.navigate(R.id.savedLocationFragment)
            }
            if (menuItem.itemId == R.id.nav_search) {
                val navController =
                    Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                navController.navigate(R.id.myMapFragment)
            }
            false
        }







}

    private fun setAppLanguage(context: Context, languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val config = Configuration()
        config.locale = locale

        context.resources.updateConfiguration(config, context.resources.displayMetrics)
        requireActivity().recreate()

        isLanguageChanged = true
    }


}