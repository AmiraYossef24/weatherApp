package countrydetails.view

import AppDB.WeatherLocalDataSource
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
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
import com.google.android.material.navigation.NavigationView
import home.view.HomeListAdapter
import home.viewModel.HomeViewModel
import home.viewModel.HomeViewModelFactory
import kotlinx.coroutines.launch
import model.WeatherRepository
import network.weatherRemoteDataSource


class CountryDetailsFragment : Fragment() {

    lateinit  var text : TextView
    lateinit var minMax : TextView
    lateinit var degree : TextView
    //lateinit var adapter: HomeListAdapter
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
//    lateinit var homeFactory : HomeViewModelFactory
//    lateinit var viewModel : HomeViewModel
    lateinit var  backgroundContainer: CoordinatorLayout
    lateinit var  weatherAnimationView: LottieAnimationView
    lateinit var  sunriseAnimationView: LottieAnimationView
    lateinit var  starsAnimationView: LottieAnimationView

    lateinit var drawer : DrawerLayout
    lateinit var myNavigationView : NavigationView

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_country_details, container, false)
    }

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

        //adapter= HomeListAdapter()
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

//        homeFactory = HomeViewModelFactory(
//            WeatherRepository.getInstance(
//                weatherRemoteDataSource.getInstance(),
//                WeatherLocalDataSource(requireContext())
//            )
//
//        )
//
//        viewModel = ViewModelProvider(this, homeFactory).get(HomeViewModel::class.java)

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

//        viewModel.weather.observe(viewLifecycleOwner) { current_weather ->
//            min=current_weather.main.temp_min
//            max=current_weather.main.temp_max
//            text.text = current_weather.name
//            minMax.text = convertToCelsiusString(min,max)
//            degree.text=current_weather.getTemperatureInCelsius()
//            date.text=current_weather.getDayOfWeek()
//            desc.text=current_weather.weather.get(0).description
//            country.text=current_weather.sys.country
//            des=current_weather.weather.get(0).description
//            updateBackgroundAnimation(des)
//
//            Log.i("TAG", "onViewCreated:  The WEAAA = $current_weather ")
//        }
        val layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL,false)
//        recyclerView.adapter = adapter
//        recyclerView.layoutManager = layoutManager

//        lifecycleScope.launch {
//            viewModel.details.observe(viewLifecycleOwner) { details ->
//                adapter.submitList(details.list)
//                sunset.text=convertToAmPmFormat(details.city.sunset)
//                sunrise.text=convertToAmPmFormat(details.city.sunrise)
//
//                Log.i(TAG, "fells like >>>>>>>>>> ${details.list.get(0).main.feels_like}")
//            }
//
//        }

//
//        lifecycleScope.launch {
//            viewModel.fiveDays.observe(viewLifecycleOwner){
//                Log.i(TAG, "the list of 5 days = : ${it.get(0).dt_txt } and second day is  = : ${it.get(1).dt_txt} and third day is = : ${it.get(2).dt_txt}")
//                if(it.isNotEmpty()){
//                    feel.text=getTemperatureInCelsius(it[0].main.feels_like)
//                    wind.text="${it[0].wind.speed} mi/h"
//                    hum.text="${it[0].main.humidity}%"
//                    uv.text="Very week"
//                    visibility.text="${it[0].visibility} km"
//                    air.text="${it[0].main.pressure} hPa"
//                    sea.text="1015 m"
//                    desc2.text=it[0].weather[0].description
//                    day1Name.text="Today"
//                    day1Des.text=it[0].weather[0].description
//                    val iconDrawable = ContextCompat.getDrawable(requireContext(), setIcon(it[0].weather[0].icon))
//                    day1Des.setCompoundDrawablesWithIntrinsicBounds(iconDrawable, null, null, null)
//                    day1Temp.text=convertToCelsiusString(it[0].main.temp_min,it[0].main.temp_min)
//
//                    day2Name.text=getDayName(it[1].dt_txt)
//                    day2Des.text=it[1].weather[0].description
//                    val iconDrawable2 = ContextCompat.getDrawable(requireContext(), setIcon(it[1].weather[0].icon))
//                    day2Des.setCompoundDrawablesWithIntrinsicBounds(iconDrawable2, null, null, null)
//                    day2Temp.text=convertToCelsiusString(it[1].main.temp_min,it[1].main.temp_min)
//
//                    day3Name.text=getDayName(it[2].dt_txt)
//                    day3Des.text=it[2].weather[0].description
//                    val iconDrawable3 = ContextCompat.getDrawable(requireContext(), setIcon(it[2].weather[0].icon))
//                    day3Des.setCompoundDrawablesWithIntrinsicBounds(iconDrawable3, null, null, null)
//                    day3Temp.text=convertToCelsiusString(it[2].main.temp_min,it[2].main.temp_min)
//
//                    day4Name.text=getDayName(it[3].dt_txt)
//                    day4Des.text=it[3].weather[0].description
//                    val iconDrawable4 = ContextCompat.getDrawable(requireContext(), setIcon(it[3].weather[0].icon))
//                    day4Des.setCompoundDrawablesWithIntrinsicBounds(iconDrawable4, null, null, null)
//                    day4Temp.text=convertToCelsiusString(it[3].main.temp_min,it[3].main.temp_min)
//
//                    day5Name.text=getDayName(it[4].dt_txt)
//                    day5Des.text=it[4].weather[0].description
//                    val iconDrawable5 = ContextCompat.getDrawable(requireContext(), setIcon(it[4].weather[0].icon))
//                    day5Des.setCompoundDrawablesWithIntrinsicBounds(iconDrawable5, null, null, null)
//                    day5Temp.text=convertToCelsiusString(it[4].main.temp_min,it[4].main.temp_min)
//
//
//                }else if(it.isEmpty()) {
//
//                }
//
//            }
//        }



    }


}