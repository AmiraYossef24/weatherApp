package utility

import model.DetailsResponse
import model.WeatherResponse

sealed class ApiState {

    data class Success (val data : WeatherResponse) : ApiState()
    data class SuccessDetails (val data : DetailsResponse) : ApiState()

    data class Failure(val msg: Throwable) : ApiState()
    data object Loading : ApiState()


}