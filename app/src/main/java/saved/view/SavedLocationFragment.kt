package saved.view

import AppDB.WeatherLocalDataSource
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.example.weatherapp.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import home.viewModel.HomeViewModel
import home.viewModel.HomeViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import model.Location
import model.WeatherRepository
import network.weatherRemoteDataSource
import saved.viewModel.SavedViewModel
import saved.viewModel.SavedViewModelFactory
import search.view.myMapFragmentDirections


class SavedLocationFragment : Fragment() {

    lateinit var animationView: LottieAnimationView
    lateinit var starsAnimationView:LottieAnimationView
    lateinit var savedRecycle : RecyclerView
    lateinit var fabBtn : FloatingActionButton
    lateinit var search : TextView
    lateinit var savedViewModelFactory: SavedViewModelFactory
    lateinit var savedViewModel: SavedViewModel
    lateinit var adapter  : SavedListAdapter
    lateinit var navController : NavController
    lateinit var myNavigationView: NavigationView
    private val sharedFlow = MutableSharedFlow<List<Location>>()
    lateinit var boxImage : ImageView
    lateinit var nodata:TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_saved_location, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        boxImage=view.findViewById(R.id.boxImageID)
        nodata=view.findViewById(R.id.naDataTxID)
        animationView=view.findViewById(R.id.savedAnimationView)
        savedRecycle=view.findViewById(R.id.savedRecycleID)
        search=view.findViewById(R.id.savedSearchID)
        fabBtn=view.findViewById(R.id.fabMapBtn)
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
        myNavigationView = view.findViewById(R.id.navigationView3ID)
        starsAnimationView=view.findViewById(R.id.starAnimationView3)
        starsAnimationView.setAnimation(R.raw.stars)
        starsAnimationView.playAnimation()

        fabBtn.setOnClickListener {
            val navController =
                Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
            navController.navigate(R.id.myMapFragment)
        }
        myNavigationView.setNavigationItemSelectedListener { menuItem ->
            if (menuItem.itemId == R.id.nav_home) {
                val action = SavedLocationFragmentDirections.actionSavedLocationFragmentToHomeFragment("","","GPS")
                val navController =
                    Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                navController.navigate(action)
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
            if (menuItem.itemId == R.id.nav_setting) {
                val navController =
                    Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                navController.navigate(R.id.settingFragment)
            }
            false
        }



        savedViewModelFactory = SavedViewModelFactory(
            WeatherRepository.getInstance(
                weatherRemoteDataSource.getInstance(),
                WeatherLocalDataSource(requireContext())
            )

        )
        savedViewModel = ViewModelProvider(this, savedViewModelFactory).get(SavedViewModel::class.java)
        savedViewModel.getAllSavedLocations()
        adapter= SavedListAdapter(requireContext(),navController,savedViewModel,viewLifecycleOwner)
        val layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL,false)
        savedRecycle.adapter = adapter
        savedRecycle.layoutManager = layoutManager

        lifecycleScope.launch {
            savedViewModel.locationList.collect() {
                if(it.count()==0){

                    boxImage.visibility=View.VISIBLE
                    nodata.visibility=View.VISIBLE
                }
                adapter.submitLocationsList(it)
            }
        }
        animationView.setAnimation(R.raw.snow_anim)
        animationView.playAnimation()



    }
//    private fun filteredStudents() {
//        val scope = CoroutineScope(Dispatchers.Main)
//
//        scope.launch {
//            sharedFlow.collect { filterText ->
//                val filteredList = withContext(Dispatchers.Default) {
//                    savedViewModel.locationList.filter { it.contains(filterText, ignoreCase = true) }
//                }
//                adapter?.submitLocationsList(filteredList)
//            }
//        }
//    }



}