package AppDB

import kotlinx.coroutines.flow.Flow
import model.Location

interface IWeatherLocalDataSource {

        suspend fun insertLocation (location : Location)
        suspend fun deleteLocation(location: Location)
        suspend fun getAllSavedLocation() : Flow<List<Location>>

}