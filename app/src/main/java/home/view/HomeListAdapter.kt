package home.view

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.databinding.TempCardBinding
import model.DetailsResponse
import model.WeatherData
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class HomeListAdapter() : ListAdapter<WeatherData, MyViewHolder>(MyDiffUtil()) {

    lateinit var binding : TempCardBinding
    private lateinit var productsList : List<DetailsResponse>


    fun submitDetailsList(detailsList: List<WeatherData>) {
        submitList(detailsList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater : LayoutInflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = TempCardBinding.inflate(inflater,parent,false)
        return MyViewHolder(binding)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val temp : WeatherData = getItem(position)
        var icon : String = getItem(position).weather[0].icon
        holder.binding.dateCardTxID.text = convertToAmPmFormat(temp.dt_txt)
        holder.binding.iconCardID .setImageResource(setIcon(icon))
        holder.binding.tempCardID.text = convertToCelsiusString(temp.main.temp)
        holder.binding.cardView.findViewById<CardView>(R.id.cardView).setBackgroundColor(Color.TRANSPARENT)

//        Glide.with(holder.binding.productImg.context)
//            .load(product.thumbnail)
//            .into(holder.binding.productImg)

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

    fun convertToCelsiusString(temp: Double): String {
        val tempMinCelsius = temp - 273.15
        // Format the temperature strings
        val formattedTempMin = String.format("%.1f", tempMinCelsius)
        // Return the formatted string with the Celsius degree symbol
        return "$formattedTempMin°C"
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun convertToAmPmFormat(dtTxt: String): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
        val localDateTime = LocalDateTime.parse(dtTxt, formatter)
        val amPmFormatter = DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH)
        return localDateTime.format(amPmFormatter)
    }

}


class MyDiffUtil : DiffUtil.ItemCallback<WeatherData>(){
    override fun areItemsTheSame(oldItem: WeatherData, newItem: WeatherData): Boolean {
        return oldItem.weather[0].id == newItem.weather[0].id
    }

    override fun areContentsTheSame(oldItem: WeatherData, newItem: WeatherData): Boolean {
        return oldItem == newItem
    }

}

class MyViewHolder ( var binding: TempCardBinding ) : RecyclerView.ViewHolder(binding.root)

