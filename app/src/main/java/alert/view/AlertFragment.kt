package alert.view

import AppDB.WeatherLocalDataSource
import alert.viewModel.AlertViewModel
import alert.viewModel.AlertViewModelFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.example.weatherapp.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.launch
import model.WeatherRepository
import network.weatherRemoteDataSource
import saved.view.SavedListAdapter
import saved.viewModel.SavedViewModel
import saved.viewModel.SavedViewModelFactory
import search.view.myMapFragmentDirections

class AlertFragment : Fragment() {


    lateinit var  weatherAnimationView: LottieAnimationView
    lateinit var starAnim  : LottieAnimationView
    lateinit var myNavigationView: NavigationView
    lateinit var addAlert : FloatingActionButton
    lateinit var alertViewModelFactory: AlertViewModelFactory
    lateinit var alertViewModel: AlertViewModel
    lateinit var adapter: AlertListAdapter
    lateinit var myRecyclerView: RecyclerView
    lateinit var alertImae:ImageView
    lateinit var noAlert : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_alert, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        alertImae=view.findViewById(R.id.alertImageID)
        noAlert=view.findViewById(R.id.noAlertTxId)
        weatherAnimationView=view.findViewById(R.id.alertAnimationView)
        myRecyclerView=view.findViewById(R.id.alertRecycleID)
        weatherAnimationView.setAnimation(R.raw.snow_anim)
        weatherAnimationView.playAnimation()
        starAnim=view.findViewById(R.id.alertStarAnimationView)
        starAnim.setAnimation(R.raw.stars)
        starAnim.playAnimation()
        myNavigationView=view.findViewById(R.id.alertNavigationView)
        addAlert=view.findViewById(R.id.fabBtn2)
        alertViewModelFactory = AlertViewModelFactory(
            WeatherRepository.getInstance(
                weatherRemoteDataSource.getInstance(),
                WeatherLocalDataSource(requireContext())
            )

        )
        alertViewModel = ViewModelProvider(this, alertViewModelFactory).get(AlertViewModel::class.java)
        alertViewModel.getAllSavedCalender()


        adapter= AlertListAdapter(alertViewModel)
        val layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL,false)
        myRecyclerView.adapter = adapter
        myRecyclerView.layoutManager = layoutManager

        lifecycleScope.launch {
            alertViewModel.calenderList.collect() {
                if(it.count()==0){
                    alertImae.visibility=View.VISIBLE
                    noAlert.visibility=View.VISIBLE
                }
                adapter.submitDetailsList(it)
            }
        }

        addAlert.setOnClickListener {

            val navController =
                Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
            navController.navigate(R.id.myMapFragmentTwo)
        }

        myNavigationView.setNavigationItemSelectedListener { menuItem ->
            if (menuItem.itemId == R.id.nav_home) {
                val action = AlertFragmentDirections.actionAlertFragmentToHomeFragment("","","GPS")
                val navController =
                    Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                navController.navigate(action)
            }
            if (menuItem.itemId == R.id.nav_search) {
                val navController =
                    Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                navController.navigate(R.id.myMapFragment)
            }
            if (menuItem.itemId == R.id.nav_setting) {
                val navController =
                    Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                navController.navigate(R.id.settingFragment)
            }
            if (menuItem.itemId == R.id.nav_save) {
                val navController =
                    Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                navController.navigate(R.id.savedLocationFragment)
            }
            false
        }
    }


}