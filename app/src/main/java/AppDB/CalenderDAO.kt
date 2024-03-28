package AppDB

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import model.Calender
import model.Location

@Dao
interface CalenderDAO {

        @Query("SELECT * FROM calender_table")
        fun getAllCalender() : Flow<List<Calender>>

        @Insert(onConflict= OnConflictStrategy.IGNORE)
        fun insert(calender: Calender)

        @Delete
        fun delete(calender: Calender)

}