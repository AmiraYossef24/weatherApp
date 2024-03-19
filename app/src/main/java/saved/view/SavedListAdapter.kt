package saved.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.databinding.SavedItemBinding
import com.example.weatherapp.databinding.TempCardBinding
import model.DetailsResponse
import model.Location
import model.WeatherData
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class SavedListAdapter(private val navController: NavController): ListAdapter<Location, MyViewHolder>(MyDiffUtil()) {

    lateinit var binding : SavedItemBinding
    private lateinit var productsList : List<Location>


    fun submitLocatinosList(locations: List<Location>) {
        submitList(locations)
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

            navController.navigate(R.id.countryDetailsFragment)
        }


    }

    fun setAnim(des : String ) : Int {
        if (des == "clear sky"){
            return R.raw.snow_anim
        }

        return R.drawable.sunny
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

