package model

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity(tableName = "calender_table")
class Calender(
    @PrimaryKey
    @NonNull
    var date : String,
    @NotNull
    var time  :String,
    @NonNull
    var countryName : String,
    @NotNull
    var late : Double,
    @NotNull
    var langi : Double
)
