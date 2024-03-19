package model

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull


@Entity(tableName = "locations_table")
class Location (
    @PrimaryKey
    @NonNull
     var countryName : String,
    @NotNull
    var late : Double,
    @NotNull
    var lon : Double

)