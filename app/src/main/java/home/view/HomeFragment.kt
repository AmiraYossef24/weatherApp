package home.view

import AppDB.WeatherLocalDataSource
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
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
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.example.weatherapp.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.navigation.NavigationView
import home.viewModel.HomeViewModel
import home.viewModel.HomeViewModelFactory
import kotlinx.coroutines.launch
import model.WeatherRepository
import network.weatherRemoteDataSource
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale


class HomeFragment : Fragment() {


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

    lateinit var feel  :TextView
    lateinit var wind : TextView
    lateinit var hum  :TextView
    lateinit var uv : TextView
    lateinit var visibility : TextView
    lateinit var air : TextView
    lateinit var sea : TextView
    lateinit var desc2 : TextView

    lateinit  var sunset : TextView
    lateinit var sunrise : TextView

    lateinit var menu_icon : ImageView
    lateinit var homeFactory : HomeViewModelFactory
    lateinit var viewModel : HomeViewModel
    lateinit var  backgroundContainer: CoordinatorLayout
    lateinit var  weatherAnimationView: LottieAnimationView
    lateinit var  sunriseAnimationView: LottieAnimationView
    lateinit var  starsAnimationView: LottieAnimationView

    lateinit var drawer : DrawerLayout
    lateinit var myNavigationView : NavigationView

    var lan : Double = 0.0
    var lat : Double = 0.0
    lateinit  var des : String
    var max : Double =0.0
    var min : Double = 0.0
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val REQUEST_LOCATION_CODE = 1001 // Your request code
    private val API_KEY="af0b74520668db5033dea0b93e9a70c3"
    private val TAG="HomeFragment"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        text = view.findViewById(R.id.myTxView)
        degree=view.findViewById(R.id.degreeTxID)
        date=view.findViewById(R.id.dateTxId)
        minMax=view.findViewById(R.id.maxMinTxViewID)
        desc=view.findViewById(R.id.descTxId)
        feel=view.findViewById(R.id.big_textCard1)
        wind=view.findViewById(R.id.big_textCard2)
        hum=view.findViewById(R.id.btextCard3)
        uv=view.findViewById(R.id.btextCard4)
        visibility=view.findViewById(R.id.btextCard5)
        air=view.findViewById(R.id.btextCard6)
        sea=view.findViewById(R.id.btextCard7)
        desc2=view.findViewById(R.id.btextCard8)

        day1Name=view.findViewById(R.id.day1NameTx)
        day1Des=view.findViewById(R.id.day1DesTx)
        day1Temp=view.findViewById(R.id.day1TempTx)

        day2Name=view.findViewById(R.id.day2NameTx)
        day2Des=view.findViewById(R.id.day2DesTx)
        day2Temp=view.findViewById(R.id.day2TempTx)

        day3Name=view.findViewById(R.id.day3NameTx)
        day3Des=view.findViewById(R.id.day3DesTx)
        day3Temp=view.findViewById(R.id.day3TempTx)

        day4Name=view.findViewById(R.id.day4NameTx)
        day4Des=view.findViewById(R.id.day4DesTx)
        day4Temp=view.findViewById(R.id.day4TempTx)

        day5Name=view.findViewById(R.id.day5NameTx)
        day5Des=view.findViewById(R.id.day5DesTx)
        day5Temp=view.findViewById(R.id.day5TempTx)

        sunset=view.findViewById(R.id.sunsetTx)
        sunrise=view.findViewById(R.id.sunriseTX)

        drawer=view.findViewById(R.id.drawerLayoutID)
        menu_icon=view.findViewById(R.id.menuIconID)
        myNavigationView = view.findViewById(R.id.navigationViewID)
        val toggle = ActionBarDrawerToggle(requireActivity() , drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)
        toggle.syncState()

        adapter= HomeListAdapter()
        recyclerView=view.findViewById(R.id.tempRecycle)
        country=view.findViewById(R.id.countryTxID)
        backgroundContainer=view.findViewById(R.id.backgroundContainer)
        weatherAnimationView=view.findViewById(R.id.weatherAnimationView)
        sunriseAnimationView=view.findViewById(R.id.sunriseAnimationView)
        starsAnimationView=view.findViewById(R.id.starAnimationView)
        sunriseAnimationView.setAnimation(R.raw.sunrise_anim)
        sunriseAnimationView.playAnimation()
        starsAnimationView.setAnimation(R.raw.stars)
        starsAnimationView.playAnimation()


        homeFactory = HomeViewModelFactory(
            WeatherRepository.getInstance(
                weatherRemoteDataSource.getInstance(),
                WeatherLocalDataSource(requireContext())
            )

        )

        viewModel = ViewModelProvider(this, homeFactory).get(HomeViewModel::class.java)

        menu_icon.setOnClickListener {
            if (drawer.isDrawerOpen(GravityCompat.END)) {
                drawer.closeDrawer(GravityCompat.END)
            } else {
                drawer.openDrawer(GravityCompat.END)
            }
        }
        myNavigationView.setNavigationItemSelectedListener { menuItem ->
            if (menuItem.itemId == R.id.nav_search) {
                val navController = findNavController(requireActivity(), R.id.nav_host_fragment)
                navController.navigate(R.id.myMapFragment)
            }
            if (menuItem.itemId == R.id.nav_save) {
                val navController = findNavController(requireActivity(), R.id.nav_host_fragment)
                navController.navigate(R.id.savedLocationFragment)
            }
            false
        }

        viewModel.weather.observe(viewLifecycleOwner) { current_weather ->
            min=current_weather.main.temp_min
            max=current_weather.main.temp_max
            text.text = current_weather.name
            minMax.text = convertToCelsiusString(min,max)
            degree.text=current_weather.getTemperatureInCelsius()
            date.text=current_weather.getDayOfWeek()
            desc.text=current_weather.weather.get(0).description
            country.text=current_weather.sys.country
            des=current_weather.weather.get(0).description
            updateBackgroundAnimation(des)

            Log.i("TAG", "onViewCreated:  The WEAAA = $current_weather ")
        }
        val layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL,false)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = layoutManager

        lifecycleScope.launch {
            viewModel.details.observe(viewLifecycleOwner) { details ->
                adapter.submitList(details.list)
                sunset.text=convertToAmPmFormat(details.city.sunset)
                sunrise.text=convertToAmPmFormat(details.city.sunrise)

                Log.i(TAG, "fells like >>>>>>>>>> ${details.list.get(0).main.feels_like}")
            }

        }

        lifecycleScope.launch {
            viewModel.fiveDays.observe(viewLifecycleOwner){
                Log.i(TAG, "the list of 5 days = : ${it.get(0).dt_txt } and second day is  = : ${it.get(1).dt_txt} and third day is = : ${it.get(2).dt_txt}")
                if(it.isNotEmpty()){
                    feel.text=getTemperatureInCelsius(it[0].main.feels_like)
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
                    day1Temp.text=convertToCelsiusString(it[0].main.temp_min,it[0].main.temp_min)

                    day2Name.text=getDayName(it[1].dt_txt)
                    day2Des.text=it[1].weather[0].description
                    val iconDrawable2 = ContextCompat.getDrawable(requireContext(), setIcon(it[1].weather[0].icon))
                    day2Des.setCompoundDrawablesWithIntrinsicBounds(iconDrawable2, null, null, null)
                    day2Temp.text=convertToCelsiusString(it[1].main.temp_min,it[1].main.temp_min)

                    day3Name.text=getDayName(it[2].dt_txt)
                    day3Des.text=it[2].weather[0].description
                    val iconDrawable3 = ContextCompat.getDrawable(requireContext(), setIcon(it[2].weather[0].icon))
                    day3Des.setCompoundDrawablesWithIntrinsicBounds(iconDrawable3, null, null, null)
                    day3Temp.text=convertToCelsiusString(it[2].main.temp_min,it[2].main.temp_min)

                    day4Name.text=getDayName(it[3].dt_txt)
                    day4Des.text=it[3].weather[0].description
                    val iconDrawable4 = ContextCompat.getDrawable(requireContext(), setIcon(it[3].weather[0].icon))
                    day4Des.setCompoundDrawablesWithIntrinsicBounds(iconDrawable4, null, null, null)
                    day4Temp.text=convertToCelsiusString(it[3].main.temp_min,it[3].main.temp_min)

                    day5Name.text=getDayName(it[4].dt_txt)
                    day5Des.text=it[4].weather[0].description
                    val iconDrawable5 = ContextCompat.getDrawable(requireContext(), setIcon(it[4].weather[0].icon))
                    day5Des.setCompoundDrawablesWithIntrinsicBounds(iconDrawable5, null, null, null)
                    day5Temp.text=convertToCelsiusString(it[4].main.temp_min,it[4].main.temp_min)


                }else if(it.isEmpty()) {

                }

            }
        }


        if (isLocationEnabled()) {
            if (checkPermissions()) {
                getFreshLocation()
            } else {
                requestPermissions(
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_LOCATION_CODE
                )
            }
        } else {
            enableLocationServices()
        }

    }



    fun updateBackgroundAnimation(weatherCondition: String ) {
//        val animationResId = when (weatherCondition) {
//            "clear sky" -> R.raw.snowy_anim
//            else -> R.raw.snowy_anim
//        }
        weatherAnimationView.setAnimation(R.raw.snow_anim)
        weatherAnimationView.playAnimation()
    }



    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_LOCATION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getFreshLocation()
            }
        }
    }

    private fun checkPermissions(): Boolean {
        return (checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
    }

    @SuppressLint("MissingPermission")
    private fun getFreshLocation() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        fusedLocationProviderClient.requestLocationUpdates(
            LocationRequest.create().apply {
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            },
            object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    super.onLocationResult(locationResult)
                    val location = locationResult.lastLocation
                    if (location != null) {
                        lan=location.longitude
                        lat=location.latitude
                        viewModel.getCurrentWeather(lan,lat,API_KEY)
                        viewModel.getWeatherDetails(lan,lat,API_KEY)
                        viewModel.getFiveDays(lan,lat, API_KEY)

                    }
                    Log.i("TAG", "lang = : "+ location?.longitude.toString())
                    Log.i("TAG", "lat = : "+location?.latitude.toString())
                    text.text=location?.latitude.toString() + " and lang = " + location?.longitude.toString()
                    fusedLocationProviderClient.removeLocationUpdates(this)
                }
            },
            Looper.getMainLooper()
        )
    }

    private fun enableLocationServices() {
        Toast.makeText(requireContext(), "Turn On Location", Toast.LENGTH_LONG).show();
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        startActivity(intent)
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    fun getAddressFromLocation(context: Context, latitude: Double, longitude: Double): String {
        val geocoder = Geocoder(context, requireContext().resources.configuration.locale)
        val addresses: List<Address>? = geocoder.getFromLocation(latitude, longitude, 1)

        return if (addresses != null && addresses.isNotEmpty()) {
            val address: Address = addresses[0]
            val sb = StringBuilder()
            for (i in 0..address.maxAddressLineIndex) {
                sb.append(address.getAddressLine(i)).append("\n")
            }
            sb.toString()
        } else {
            "No address found"
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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun convertToAmPmFormat(timestamp: Long): String {
        val localDateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp), ZoneId.systemDefault())
        val formatter = DateTimeFormatter.ofPattern("hh:mm a")
        return localDateTime.format(formatter)
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

}