package setting.view

import AppDB.WeatherLocalDataSource
import alert.viewModel.AlertViewModel
import alert.viewModel.AlertViewModelFactory
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.airbnb.lottie.LottieAnimationView
import com.example.weatherapp.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import home.viewModel.HomeViewModel
import home.viewModel.HomeViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import model.Calender
import model.Location
import model.NotificationWorker
import model.WeatherRepository
import network.weatherRemoteDataSource
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

class MapFragmentThree : Fragment() , OnMapReadyCallback {

    lateinit var searchTx : AutoCompleteTextView
    lateinit var menu : ImageView
    private lateinit var gMap: GoogleMap
    lateinit var map : FrameLayout
    lateinit var viewMap : MapView
    lateinit var drawer : DrawerLayout
    lateinit var myNavigationView : NavigationView
    lateinit var starsAnimationView : LottieAnimationView
    lateinit var calenderFactory : AlertViewModelFactory
    lateinit var viewModel : AlertViewModel
    lateinit var homeViewModel: HomeViewModel
    lateinit var homeViewModelFactory: HomeViewModelFactory
    private val sharedFlow = MutableSharedFlow<String>()
    private val API_KEY="af0b74520668db5033dea0b93e9a70c3"
    lateinit var des : String





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_map_three, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        viewMap= view.findViewById(R.id.mapViewID3)!!
        viewMap.onCreate(savedInstanceState)
        viewMap.onResume()
        viewMap.getMapAsync(this)
        searchTx=view.findViewById(R.id.autocomplete_country3)
        drawer=view.findViewById(R.id.mapFra3DrawerID)
        starsAnimationView=view.findViewById(R.id.mapFraStarID)
        starsAnimationView.setAnimation(R.raw.stars)
        starsAnimationView.playAnimation()
        myNavigationView = view.findViewById(R.id.mapFra3NavigationID)


        homeViewModelFactory= HomeViewModelFactory(
            WeatherRepository.getInstance(
                weatherRemoteDataSource.getInstance(),
                WeatherLocalDataSource(requireContext())
            ))

        homeViewModel= ViewModelProvider(this,homeViewModelFactory).get(HomeViewModel::class.java)

        val toggle = ActionBarDrawerToggle(requireActivity() , drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)
        toggle.syncState()

        val countries: Array<out String> = resources.getStringArray(R.array.countries_array)
        ArrayAdapter<String>(requireContext(), android.R.layout.simple_list_item_1, countries).also { adapter ->
            searchTx.setAdapter(adapter)
        }


        searchTx.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                lifecycleScope.launch {
                    // Check if the text input is empty or doesn't refer to a valid country name
                    if (!s.isNullOrBlank()) {
                        goToLocation(s.toString())
                    } else {
                        gMap.clear()
                    }
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })

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
            if (menuItem.itemId == R.id.nav_alert) {
                val navController =
                    Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                navController.navigate(R.id.alertFragment)
            }
            false
        }

    }


    private suspend fun goToLocation(countryName: String) {
        val latLon = getLatLongFromCountryName(requireContext(), countryName)
        if (latLon != null) {
            addMarkerToMap(gMap, latLon.first, latLon.second, countryName)
        } else {
            gMap.clear()
        }
    }

    override fun onMapReady(map: GoogleMap) {
        gMap = map
        // Set up map listeners and interactions
        gMap.setOnMapClickListener { latLng ->
            addMarker(latLng)
            val rootView = requireView()
            val snackbar = Snackbar.make(rootView, "Do you want to select this Location ?", Snackbar.LENGTH_SHORT)
                .setActionTextColor(Color.WHITE)
                .setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.blue))

            snackbar.setAction("Dismiss") {
                snackbar.dismiss()
            }

            snackbar.setAction("Select") {
                val geocoder = Geocoder(requireContext(), Locale.getDefault())
                val addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
                if (addresses != null) {
                    if (addresses.isNotEmpty()) {
                        val address = addresses[0]
                        val countryName = address.countryName
                        val locality = address.locality // City
                        val subLocality = address.subLocality // District or Village
                        val adminArea = address.adminArea // Governorate
                        val locationName = if (!subLocality.isNullOrEmpty()) {
                            subLocality
                        } else if (!locality.isNullOrEmpty()) {
                            locality
                        } else {
                            adminArea
                        } ?: countryName // If no detailed location information is available, fallback to country name
                        val location = Location(locationName, latLng.latitude, latLng.longitude)
                        homeViewModel.getCurrentWeather(latLng.latitude,latLng.longitude,API_KEY,"metric","en")

//                        homeViewModel.weather.observe(viewLifecycleOwner){
//                            des= it.weather[0].description
//                        }

                        Toast.makeText(requireContext(), "Inserted $locationName", Toast.LENGTH_SHORT).show()
                        Log.i("TAG", "lat from map fragment =: ${latLng.latitude} ")
                        Log.i("TAG", "lon from map fragment =: ${latLng.longitude} ")

                    } else {
                        Toast.makeText(requireContext(), "No address found for the provided location", Toast.LENGTH_LONG).show()
                    }
                }

            }


            snackbar.show()
            // Perform reverse geocoding to get the address from coordinates

        }
    }

    private fun addMarker(latLng: LatLng) {
        gMap.clear()
        gMap.addMarker(MarkerOptions().position(latLng).title("Selected Location"))
        gMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
    }
    suspend fun getLatLongFromCountryName(context: Context, countryName: String): Pair<Double, Double>? {
        return withContext(Dispatchers.IO) {
            val geocoder = Geocoder(context)
            try {
                val addresses: List<Address> = geocoder.getFromLocationName(countryName, 1)!!
                if (addresses.isNotEmpty()) {
                    val lat = addresses[0].latitude
                    val long = addresses[0].longitude
                    Pair(lat, long)
                } else {
                    null
                }
            } catch (e: IOException) {
                e.printStackTrace()
                null
            }
        }
    }

    fun addMarkerToMap(googleMap: GoogleMap, lat: Double, lon: Double, title: String) {
        val location = LatLng(lat, lon)
        googleMap.clear()
        googleMap.addMarker(MarkerOptions().position(location).title(title))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location,5f))
    }

    fun dateStringToMillis(dateString: String): Long {
        val dateFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy")
        val date: Date = dateFormat.parse(dateString)
        return date.time
    }

}