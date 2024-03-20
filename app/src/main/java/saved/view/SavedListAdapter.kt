package saved.view

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.databinding.SavedItemBinding
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import model.Location
import saved.viewModel.SavedViewModel

class SavedListAdapter(
    private val context: Context,
    private val navController: NavController,
    private val viewModel: SavedViewModel,
    private val lifecycleOwner: LifecycleOwner// Add ViewModel parameter
) : ListAdapter<Location, MyViewHolder>(MyDiffUtil()) {
    lateinit var binding : SavedItemBinding
    private lateinit var productsList : List<Location>


    fun submitLocationsList(locationsFlow: Flow<List<Location>>) {
        lifecycleOwner.lifecycleScope.launch { // Use lifecycleOwner to access lifecycleScope
            locationsFlow.collect { locations ->
                submitList(locations)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater : LayoutInflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = SavedItemBinding.inflate(inflater,parent,false)
        return MyViewHolder(binding)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val location : Location = getItem(position)

        holder.binding.cuntryNameTxID.text = location.countryName
        holder.binding.cardItemID.setOnClickListener {

            val action = SavedLocationFragmentDirections.actionSavedLocationFragmentToCountryDetailsFragment2(
                location.late.toString(),
                location.lon.toString(),
                location.countryName
            )
            it.findNavController().navigate(action)
            Log.i("TAG", "lat from listAdapter = : ${location.late} ")
            Log.i("TAG", "lon from listAdapter = : ${location.lon.toFloat()} ")
            Log.i("TAG", "cuntry name from listAdapter = : ${location.countryName}")

        }
        holder.binding.cancelIconID.setOnClickListener {
            viewModel.delete(location)
        }

    }



}


class MyDiffUtil : DiffUtil.ItemCallback<Location>(){
    override fun areItemsTheSame(oldItem: Location, newItem: Location): Boolean {
        return oldItem.countryName == newItem.countryName
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: Location, newItem: Location): Boolean {
        return oldItem == newItem
    }

}

class MyViewHolder ( var binding: SavedItemBinding) : RecyclerView.ViewHolder(binding.root)

