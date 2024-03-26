package AppDB

import kotlinx.coroutines.flow.Flow
import model.Calender
import model.Location

interface IWeatherLocalDataSource {

        suspend fun insertLocation (location : Location)
        suspend fun deleteLocation(location: Location)
        suspend fun getAllSavedLocation() : Flow<List<Location>>
        suspend fun getAllCalender() : Flow<List<Calender>>
        suspend fun insertCalender(calender: Calender)
        suspend fun deleteCalender(calender: Calender)

}