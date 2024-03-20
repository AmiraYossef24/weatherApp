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
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.example.weatherapp.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
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


class SavedLocationFragment : Fragment() {

    lateinit var animationView: LottieAnimationView
    lateinit var savedRecycle : RecyclerView
    lateinit var fabBtn : FloatingActionButton
    lateinit var search : EditText
    lateinit var savedViewModelFactory: SavedViewModelFactory
    lateinit var savedViewModel: SavedViewModel
    lateinit var adapter  : SavedListAdapter
    lateinit var navController : NavController
    private val sharedFlow = MutableSharedFlow<List<Location>>()


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
        animationView=view.findViewById(R.id.savedAnimationView)
        savedRecycle=view.findViewById(R.id.savedRecycleID)
        search=view.findViewById(R.id.savedSearchID)
        fabBtn=view.findViewById(R.id.fabMapBtn)
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)


        fabBtn.setOnClickListener {
            val navController =
                Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
            navController.navigate(R.id.myMapFragment)
        }

        search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filteredStudents()
                lifecycleScope.launch {
                    sharedFlow.emit(s.toString())
                }
            }



            override fun afterTextChanged(s: Editable?) {}
        })


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

//        lifecycleScope.launch {
//            savedViewModel.locationList.collect() {
//                adapter.submitLocationsList(it)
//            }
//        }
        animationView.setAnimation(R.raw.snow_anim)
        animationView.playAnimation()

    }
    private fun filteredStudents() {
        val scope = CoroutineScope(Dispatchers.Main)

        scope.launch {
            sharedFlow.collect { filterText ->
                val filteredList = withContext(Dispatchers.Default) {
                    savedViewModel.locationList.filter { it.contains(filterText, ignoreCase = true) }
                }
                adapter?.submitLocationsList(filteredList)
            }
        }
    }



}