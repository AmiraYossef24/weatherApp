package AppDB

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import model.Location

@Dao
interface LocationDAO {

    @Query("SELECT * FROM locations_table")
    fun getAllLocation() : Flow<List<Location>>

    @Insert(onConflict= OnConflictStrategy.IGNORE)
    fun insert(location : Location)

    @Delete
    fun delete(location : Location)
}