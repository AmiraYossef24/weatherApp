package search.view

import AppDB.WeatherLocalDataSource
import android.content.Context
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import model.Location
import model.WeatherRepository
import network.weatherRemoteDataSource
import search.viewModel.MapFragmentViewModel
import search.viewModel.MapFragmentViewModelFactory
import java.io.IOException
import java.util.Locale


class myMapFragment : Fragment() ,OnMapReadyCallback {

    lateinit var searchTx : AutoCompleteTextView
    lateinit var menu : ImageView
    private lateinit var gMap: GoogleMap
    lateinit var map : FrameLayout
    lateinit var viewMap : MapView
    lateinit var drawer : DrawerLayout
    lateinit var myNavigationView : NavigationView
    lateinit var starsAnimationView : LottieAnimationView
    lateinit var locationFactory : MapFragmentViewModelFactory
    lateinit var viewModel : MapFragmentViewModel
    private val sharedFlow = MutableSharedFlow<String>()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_my_map, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        viewMap= view.findViewById(R.id.mapViewID)!!
        viewMap.onCreate(savedInstanceState)
        viewMap.onResume()
        viewMap.getMapAsync(this)
        searchTx=view.findViewById(R.id.autocomplete_country)
        drawer=view.findViewById(R.id.drawerLayout2ID)
        starsAnimationView=view.findViewById(R.id.starAnimationView2)
        starsAnimationView.setAnimation(R.raw.stars)
        starsAnimationView.playAnimation()


        myNavigationView = view.findViewById(R.id.navigationView2ID)
        locationFactory= MapFragmentViewModelFactory(
            WeatherRepository.getInstance(
                weatherRemoteDataSource.getInstance(),
                WeatherLocalDataSource(requireContext())
            ))

        viewModel= ViewModelProvider(this,locationFactory).get(MapFragmentViewModel::class.java)

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
                Toast.makeText(requireContext(),"home clicked ",Toast.LENGTH_LONG).show()
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
            val snackbar = Snackbar.make(rootView, "Do you to save this Location ?", Snackbar.LENGTH_INDEFINITE)
                .setActionTextColor(Color.WHITE)
                .setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.blue))

            snackbar.setAction("Dismiss") {
                snackbar.dismiss()
            }

            snackbar.setAction("Save") {
                val geocoder = Geocoder(requireContext(), Locale.getDefault())
                val addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
                val countryName = addresses?.firstOrNull()?.countryName
                if (!countryName.isNullOrEmpty()) {
                    val location = Location(countryName,latLng.latitude,latLng.longitude)
                    viewModel.insert(location)
                    Toast.makeText(requireContext(),"isert ${countryName} done",Toast.LENGTH_LONG).show()

                }else{
                    Toast.makeText(requireContext(),"Incorrect location",Toast.LENGTH_LONG).show()

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




}