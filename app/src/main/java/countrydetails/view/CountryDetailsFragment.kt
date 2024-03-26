package countrydetails.view

import AppDB.WeatherLocalDataSource
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.example.weatherapp.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.material.navigation.NavigationView
import home.view.HomeListAdapter
import home.viewModel.HomeViewModel
import home.viewModel.HomeViewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import model.WeatherRepository
import network.weatherRemoteDataSource
import setting.viewModel.SettingViewModel
import setting.viewModel.SettingViewModelFactory
import java.io.IOException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale


class CountryDetailsFragment : Fragment() {

    lateinit  var text : TextView
    lateinit var minMax : TextView
    lateinit var degree : TextView
    lateinit var adapter: HomeListAdapter
    lateinit var date : TextView
    lateinit var recyclerView: RecyclerView
    lateinit var desc : TextView
    lateinit var country : TextView
    lateinit var day1Name  : TextView
    lateinit var day1Des  : TextView
    lateinit var day1Temp  : TextView

    lateinit var day2Name  : TextView
    lateinit var day2Des  : TextView
    lateinit var day2Temp  : TextView

    lateinit var day3Name  : TextView
    lateinit var day3Des  : TextView
    lateinit var day3Temp  : TextView

    lateinit var day4Name  : TextView
    lateinit var day4Des  : TextView
    lateinit var day4Temp  : TextView

    lateinit var day5Name  : TextView
    lateinit var day5Des  : TextView
    lateinit var day5Temp  : TextView

    lateinit var feel  : TextView
    lateinit var wind : TextView
    lateinit var hum  : TextView
    lateinit var uv : TextView
    lateinit var visibility : TextView
    lateinit var air : TextView
    lateinit var sea : TextView
    lateinit var desc2 : TextView

    lateinit  var sunset : TextView
    lateinit var sunrise : TextView

    lateinit var menu_icon : ImageView
    lateinit var homeFactory : HomeViewModelFactory
    lateinit var  viewModel : HomeViewModel
    lateinit var  backgroundContainer: CoordinatorLayout
    lateinit var  weatherAnimationView: LottieAnimationView
    lateinit var  sunriseAnimationView: LottieAnimationView
    lateinit var  starsAnimationView: LottieAnimationView

    lateinit var drawer : DrawerLayout
    lateinit var myNavigationView : NavigationView

    private lateinit var navController: NavController

    var lan : Double = 0.0
    var lat : Double = 0.0
    lateinit  var des : String
    var max : Double =0.0
    var min : Double = 0.0
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val API_KEY="af0b74520668db5033dea0b93e9a70c3"
    private val TAG="CountryDetailsFragment"
    lateinit var settingFactory : SettingViewModelFactory
    lateinit var settingViewModel : SettingViewModel
    private var newLang :String =""
    private var newTemp :String=""
    lateinit var newLocation :String
    lateinit var newWindSpeed:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onStart() {
        super.onStart()
        settingViewModel.emitChangingSetting()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_country_details, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        text = view.findViewById(R.id.myDetailsTxView)
        degree=view.findViewById(R.id.degreeDetailsTxID)
        date=view.findViewById(R.id.dateDetailsTxId)
        minMax=view.findViewById(R.id.maxMinDetailsTxViewID)
        desc=view.findViewById(R.id.descDetailsTxId)
        feel=view.findViewById(R.id.big_DetailstextCard1)
        wind=view.findViewById(R.id.big_DetailstextCard2)
        hum=view.findViewById(R.id.btextDetailsCard3)
        uv=view.findViewById(R.id.btextDetailsCard4)
        visibility=view.findViewById(R.id.btextDetailsCard5)
        air=view.findViewById(R.id.btextDetailsCard6)
        sea=view.findViewById(R.id.btextDetailsCard7)
        desc2=view.findViewById(R.id.btextDetailsCard8)

        day1Name=view.findViewById(R.id.day1NameDetailsTx)
        day1Des=view.findViewById(R.id.day1DesDetailsTx)
        day1Temp=view.findViewById(R.id.day1TempDetailsTx)

        day2Name=view.findViewById(R.id.day2NameDetailsTx)
        day2Des=view.findViewById(R.id.day2DesDetailsTx)
        day2Temp=view.findViewById(R.id.day2TempDetailsTx)

        day3Name=view.findViewById(R.id.day3NameDetailsTx)
        day3Des=view.findViewById(R.id.day3DesDetailsTx)
        day3Temp=view.findViewById(R.id.day3TempDetailsTx)

        day4Name=view.findViewById(R.id.day4NameDetailsTx)
        day4Des=view.findViewById(R.id.day4DesDetailsTx)
        day4Temp=view.findViewById(R.id.day4TempDetailsTx)

        day5Name=view.findViewById(R.id.day5NameDetailsTx)
        day5Des=view.findViewById(R.id.day5DesDetailsTx)
        day5Temp=view.findViewById(R.id.day5TempDetailsTx)

        sunset=view.findViewById(R.id.sunsetDetailsTx)
        sunrise=view.findViewById(R.id.sunriseDetailsTX)

        drawer=view.findViewById(R.id.detailsDrawerLayoutID)
        menu_icon=view.findViewById(R.id.detailsMenuIconID)
        myNavigationView = view.findViewById(R.id.navigationViewDetailsID)
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
        val toggle = ActionBarDrawerToggle(requireActivity() , drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)
        toggle.syncState()

        adapter= HomeListAdapter()
        recyclerView=view.findViewById(R.id.tempDetailsRecycle)
        country=view.findViewById(R.id.countryDetailsTxID)
        backgroundContainer=view.findViewById(R.id.detailsBackgroundContainer)
        weatherAnimationView=view.findViewById(R.id.detailsAnimationView)
        sunriseAnimationView=view.findViewById(R.id.sunriseAnimationViewDetails)
        starsAnimationView=view.findViewById(R.id.starAnimationViewDetails)
        sunriseAnimationView.setAnimation(R.raw.sunrise_anim)
        sunriseAnimationView.playAnimation()
        starsAnimationView.setAnimation(R.raw.stars)
        starsAnimationView.playAnimation()
        weatherAnimationView.setAnimation(R.raw.snow_anim)
        weatherAnimationView.playAnimation()
        settingFactory = SettingViewModelFactory(requireActivity().application)
        settingViewModel = ViewModelProvider(this, settingFactory).get(SettingViewModel::class.java)

        homeFactory = HomeViewModelFactory(
            WeatherRepository.getInstance(
                weatherRemoteDataSource.getInstance(),
                WeatherLocalDataSource(requireContext())
            )

        )
        viewModel = ViewModelProvider(this, homeFactory).get(HomeViewModel::class.java)


        var countryName = arguments?.let { CountryDetailsFragmentArgs.fromBundle(it).countryName }
        Log.i(TAG, "country name fron countryDetails = : ${countryName}")
        var lat= arguments?.let { CountryDetailsFragmentArgs.fromBundle(it).lat }
        Log.i(TAG, "lat from countryDetails = : ${lat}")
        var lon= arguments?.let { CountryDetailsFragmentArgs.fromBundle(it).lon }
        Log.i(TAG, "lon from countryDetails = : ${lon}")
        var nameStr= arguments?.let { CountryDetailsFragmentArgs.fromBundle(it).countryName }



        lifecycleScope.launch {
            Log.i(TAG, "inside setting scope : ")
            settingViewModel.settingChanges.collectLatest { setting ->
                Log.i(TAG, "inside setting scope lone 221: ")

                newLang = setting.lang
                newTemp = setting.tempe
                Log.i(TAG, "newTemp = : ${newTemp}")
                newLocation = setting.loc
                newWindSpeed = setting.wind

                Log.i(
                    TAG,
                    "collectSettingsData: 1-$newLang , 2-$newTemp , 3-$newLocation , 4-$newWindSpeed "
                )
            }
        }

        Log.i(TAG, "cuntry name from details = : ${countryName}")
        if (countryName != null) {
            Log.i(TAG, "after line 231: ")
            getLocationFromAddress(requireContext(),countryName)?.let {
                getLocationFromAddress(requireContext(),countryName)?.let { it1 ->
                    viewModel.getCurrentWeather(
                        it.first,
                        it1.second,API_KEY,newTemp,newLang)

                    viewModel.getWeatherDetails(
                        it.first,
                        it1.second,API_KEY,newTemp,newLang)

                    viewModel.getFiveDays(
                        it.first,
                        it1.second,API_KEY,newTemp,newLang)

                }
            }
        }else{
            Log.i(TAG, "countryname = : ${countryName}")
        }
        menu_icon.setOnClickListener {
            if (drawer.isDrawerOpen(GravityCompat.END)) {
                drawer.closeDrawer(GravityCompat.END)
            } else {
                drawer.openDrawer(GravityCompat.END)
            }
        }
        myNavigationView.setNavigationItemSelectedListener { menuItem ->
            if (menuItem.itemId == R.id.nav_search) {
                val navController =
                    Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                navController.navigate(R.id.myMapFragment)
            }
            if (menuItem.itemId == R.id.nav_home) {
                val navController =
                    Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                navController.navigate(R.id.homeFragment)
            }
            if (menuItem.itemId == R.id.nav_save) {
                val navController =
                    Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                navController.navigate(R.id.savedLocationFragment)
            }
            false
        }


        viewModel.weather.observe(viewLifecycleOwner) { current_weather ->
            min=current_weather.main.temp_min
            max=current_weather.main.temp_max
            text.text = nameStr
            minMax.text = convertToCelsiusString(min,max)
            degree.text=current_weather.getTemperatureInCelsius()
            date.text=current_weather.getDayOfWeek()
            desc.text=current_weather.weather.get(0).description
            country.text=current_weather.sys.country

            Log.i("TAG", "onViewCreated:  The WEAAA = $current_weather ")
        }
        val layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL,false)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = layoutManager

        lifecycleScope.launch {
            viewModel.details.observe(viewLifecycleOwner) { details ->
                adapter.submitList(details.list)
//                sunset.text=convertToAmPmFormat(details.city.sunset)
//                sunrise.text=convertToAmPmFormat(details.city.sunrise)

                Log.i(TAG, "fells like >>>>>>>>>> ${details.list.get(0).main.feels_like}")
            }

        }


        lifecycleScope.launch {
            viewModel.fiveDays.observe(viewLifecycleOwner){
                Log.i(TAG, "the list of 5 days = : ${it.get(0).dt_txt } and second day is  = : ${it.get(1).dt_txt} and third day is = : ${it.get(2).dt_txt}")
                if(it.isNotEmpty()){
                    feel.text=getTemperatureInCelsius(it[0].main.feels_like)
                    Log.i(TAG, "feel text : ${getTemperatureInCelsius(it[0].main.feels_like)}")
                    wind.text="${it[0].wind.speed} mi/h"
                    hum.text="${it[0].main.humidity}%"
                    uv.text="Very week"
                    visibility.text="${it[0].visibility} km"
                    air.text="${it[0].main.pressure} hPa"
                    sea.text="1015 m"
                    desc2.text=it[0].weather[0].description
                    day1Name.text="Today"
                    day1Des.text=it[0].weather[0].description
                    val iconDrawable = ContextCompat.getDrawable(requireContext(), setIcon(it[0].weather[0].icon))
                    day1Des.setCompoundDrawablesWithIntrinsicBounds(iconDrawable, null, null, null)
                    day1Temp.text=convertToCelsiusString(it[0].main.temp_min,it[0].main.temp_max)

                    day2Name.text=getDayName(it[1].dt_txt)
                    day2Des.text=it[1].weather[0].description
                    val iconDrawable2 = ContextCompat.getDrawable(requireContext(), setIcon(it[1].weather[0].icon))
                    day2Des.setCompoundDrawablesWithIntrinsicBounds(iconDrawable2, null, null, null)
                    day2Temp.text=convertToCelsiusString(it[1].main.temp_min,it[1].main.temp_max)

                    day3Name.text=getDayName(it[2].dt_txt)
                    day3Des.text=it[2].weather[0].description
                    val iconDrawable3 = ContextCompat.getDrawable(requireContext(), setIcon(it[2].weather[0].icon))
                    day3Des.setCompoundDrawablesWithIntrinsicBounds(iconDrawable3, null, null, null)
                    day3Temp.text=convertToCelsiusString(it[2].main.temp_min,it[2].main.temp_max)

                    day4Name.text=getDayName(it[3].dt_txt)
                    day4Des.text=it[3].weather[0].description
                    val iconDrawable4 = ContextCompat.getDrawable(requireContext(), setIcon(it[3].weather[0].icon))
                    day4Des.setCompoundDrawablesWithIntrinsicBounds(iconDrawable4, null, null, null)
                    day4Temp.text=convertToCelsiusString(it[3].main.temp_min,it[3].main.temp_max)

                    day5Name.text=getDayName(it[4].dt_txt)
                    day5Des.text=it[4].weather[0].description
                    val iconDrawable5 = ContextCompat.getDrawable(requireContext(), setIcon(it[4].weather[0].icon))
                    day5Des.setCompoundDrawablesWithIntrinsicBounds(iconDrawable5, null, null, null)
                    day5Temp.text=convertToCelsiusString(it[4].main.temp_min,it[4].main.temp_max)


                }else if(it.isEmpty()) {

                    Toast.makeText(requireContext(),"is empty ",Toast.LENGTH_LONG).show()
                }

            }
        }




    }
    fun convertToCelsiusString(tempMin: Double, tempMax: Double): String {
        val tempMinCelsius = tempMin - 273.15
        val tempMaxCelsius = tempMax - 273.15

        // Format the temperature strings
        val formattedTempMin = String.format("%.1f", tempMinCelsius)
        val formattedTempMax = String.format("%.1f", tempMaxCelsius)

        // Return the formatted string with the Celsius degree symbol
        return "$formattedTempMin° / $formattedTempMax°"
    }
     fun getLocationFromAddress(context: Context, address: String): Pair<Double, Double>? {
        val geocoder = Geocoder(context)
        return try {
            val addresses: List<Address> = geocoder.getFromLocationName(address, 1)!!
            if (addresses.isNotEmpty()) {
                val latitude = addresses[0].latitude
                val longitude = addresses[0].longitude
                Pair(latitude, longitude)
            } else {
                null
            }
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun convertToAmPmFormat(dtTxt: Long): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
        val localDateTime = LocalDateTime.parse(dtTxt.toString(), formatter)
        val amPmFormatter = DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH)
        return localDateTime.format(amPmFormatter)
    }

    fun setIcon(icon : String ) : Int {
        if (icon == "04d"){
            return R.drawable._4d
        }
        if(icon=="03n" || icon =="03d"){
            return R.drawable._3n
        }
        if(icon=="01n" || icon=="01d"){
            return R.drawable._1d
        }
        if(icon=="04n"){
            return R.drawable._4n
        }
        if(icon=="02n" || icon=="02d"){
            return R.drawable._2d
        }
        if(icon=="10d"){
            return R.drawable._0d
        }
        if(icon=="10n"){
            return R.drawable._0n
        }
        return R.drawable.sunny
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun getDayName(dt: String): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val dateTime = LocalDateTime.parse(dt, formatter)
        val dayOfWeek = dateTime.dayOfWeek
        return dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())
    }

    fun getTemperatureInCelsius(temp : Double): String {
        val temperatureCelsius = temp - 273.15
        return String.format("%.1f°C", temperatureCelsius)
    }



}