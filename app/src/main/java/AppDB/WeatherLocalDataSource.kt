package AppDB

import android.content.Context
import kotlinx.coroutines.flow.Flow
import model.Location


class WeatherLocalDataSource (context : Context) : IWeatherLocalDataSource {

    private val dao : LocationDAO by lazy{
        val db : AppDataBase = AppDataBase.getInstance(context)
        db.locationDAO()
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
}