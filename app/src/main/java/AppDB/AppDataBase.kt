package AppDB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import model.Location

@Database(entities = [Location::class],version = 1)
abstract class AppDataBase : RoomDatabase() {

    abstract fun locationDAO() : LocationDAO

    companion object{
        private var instance : AppDataBase?=null

        //one thread at a time to access this method
        @Synchronized
        fun getInstance(context: Context): AppDataBase {
            return instance ?: synchronized(this) {
                val db = Room.databaseBuilder(
                    context.applicationContext,
                    AppDataBase::class.java,
                    "locations_table"
                ).fallbackToDestructiveMigration().build()
                instance = db
                db
            }
        }
    }
}