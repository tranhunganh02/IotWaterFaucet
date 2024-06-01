package Final.iotwater.domain

import Final.iotwater.data.Sensor
import Final.iotwater.data.FaucetState
import Final.iotwater.data.Humidity
import Final.iotwater.data.WateringSchedule
import Final.iotwater.data.WateringTime
import Final.iotwater.utils.ResultState
import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow


interface SensorRepository {

    fun getFaucetState(): LiveData<Boolean>
    fun getHumidity(): LiveData<Int>
    fun getLastWateringTime(): LiveData<WateringTime>
    fun getWateringSchedule(): LiveData<WateringSchedule>

    fun updateWaterPumpState(state: Boolean)

    fun updateFaucetState(newState: Boolean)

    fun updateWateringSchedule(newTime: String, day: String)

}