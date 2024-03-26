package AppDB

import android.content.Context
import kotlinx.coroutines.flow.Flow
import model.Calender
import model.Location


class WeatherLocalDataSource (context : Context) : IWeatherLocalDataSource {

    private val dao : LocationDAO by lazy{
        val db : AppDataBase = AppDataBase.getInstance(context)
        db.locationDAO()
    }
    private val calenderDao : CalenderDAO by lazy{
        val db : AppDataBase = AppDataBase.getInstance(context)
        db.calenderDAO()
    }

     override suspend fun insertLocation (location : Location){
        dao.insert(location)
    }

     override suspend fun deleteLocation(location: Location){
        dao.delete(location)
    }
     override suspend fun getAllSavedLocation() : Flow<List<Location>> {
        return dao.getAllLocation()
    }

    override suspend fun getAllCalender() : Flow<List<Calender>>{
        return calenderDao.getAllCalender()
    }

    override suspend fun insertCalender(calender: Calender){
        calenderDao.insert(calender)

    }

    override suspend fun deleteCalender(calender: Calender){
        calenderDao.delete(calender)
    }
}