package alert.view

import alert.viewModel.AlertViewModel
import android.annotation.SuppressLint
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
import com.example.weatherapp.databinding.CalenderItemBinding
import com.example.weatherapp.databinding.TempCardBinding
import model.Calender
import model.DetailsResponse
import model.WeatherData
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class AlertListAdapter (private val viewModel : AlertViewModel) : ListAdapter<Calender, MyViewHolder>(MyDiffUtil()) {

    lateinit var binding : CalenderItemBinding


    fun submitDetailsList(alertList: List<Calender>) {
        submitList(alertList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater : LayoutInflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = CalenderItemBinding.inflate(inflater,parent,false)
        return MyViewHolder(binding)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val calender : Calender = getItem(position)
        holder.binding.dateTxID.text=convertDateFormat(calender.date)
        holder.binding.timeTxID.text=convertToAmPmFormat(calender.time)
        holder.binding.countrynameTxID.text=calender.countryName
        holder.binding.cancelAlertID.setColorFilter(Color.WHITE)
        holder.binding.cancelAlertID.setOnClickListener {
            viewModel.delete(calender)
        }



    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun convertToAmPmFormat(dateString: String): String {
        // Parse the date string to a LocalDateTime object
        val dateTime = LocalDateTime.parse(dateString, DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss zzzz yyyy"))

        // Convert the LocalDateTime object to AM/PM format
        val formatter = DateTimeFormatter.ofPattern("hh:mm a")
        return formatter.format(dateTime)
    }

    fun convertDateFormat(inputDate: String): String {
        val inputFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy", Locale.ENGLISH)
        val outputFormat = SimpleDateFormat("EEE dd MMM yyyy", Locale.ENGLISH)

        val date = inputFormat.parse(inputDate)
        return outputFormat.format(date)
    }

}


class MyDiffUtil : DiffUtil.ItemCallback<Calender>(){
    override fun areItemsTheSame(oldItem: Calender, newItem: Calender): Boolean {
        return oldItem.date == newItem.date
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: Calender, newItem: Calender): Boolean {
        return oldItem == newItem
    }

}

class MyViewHolder ( var binding: CalenderItemBinding) : RecyclerView.ViewHolder(binding.root)

