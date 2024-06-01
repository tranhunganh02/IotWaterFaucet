package Final.iotwater.present

import Final.iotwater.data.WateringSchedule
import Final.iotwater.data.WateringTime
import Final.iotwater.domain.SensorRepository
import Final.iotwater.repository.FirebaseSensorRepository
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.io.IOException

class SensorViewModel(private val sensorRepository: SensorRepository) : ViewModel() {

    @Suppress("unused")
    constructor() : this(FirebaseSensorRepository(FirebaseDatabase.getInstance()))

    var uiState: UiState by mutableStateOf(UiState.Loading)
        private set

    var updateState: UiState by mutableStateOf(UiState.Loading)
        private set


    private val _faucetState = MutableLiveData<Boolean>()
    val faucetState: LiveData<Boolean>
        get() = _faucetState


    private val _humidity = MutableLiveData<Int>()
    val humidity: LiveData<Int>
        get() = _humidity

    private val _lastWateringTime = MutableLiveData<WateringTime>()
    val lastWateringTime: MutableLiveData<WateringTime>
        get() = _lastWateringTime

    private val _wateringSchedule= MutableLiveData<WateringSchedule>()
        val wateringSchedule: LiveData<WateringSchedule>
        get() = _wateringSchedule

    init {
        getSensorData()
    }

    private fun getSensorData() {
        viewModelScope.launch {
        try {
            uiState = UiState.Loading
            sensorRepository.getFaucetState().observeForever { faucetState ->
                _faucetState.value = faucetState
            }

            sensorRepository.getHumidity().observeForever { humidity ->
                _humidity.value = humidity
            }


            sensorRepository.getLastWateringTime().observeForever { wateringTime ->
                // Update the value of _lastWateringTime when the data changes
                _lastWateringTime.value = wateringTime
                Log.d("fbb",   wateringTime.time)
            }
            sensorRepository.getWateringSchedule().observeForever { wateringSchedule ->
                _wateringSchedule.value =  (wateringSchedule)
            }

            uiState = UiState.Success
        } catch (e: IOException) {
           uiState = UiState.Error
        }

    }}
//    val wateringSchedule: LiveData<WateringSchedule> = sensorRepository.getWateringSchedule()


    fun updateFaucetState(newState: Boolean) {
      try {
          updateState = UiState.Loading
          sensorRepository.updateFaucetState(newState)
          updateState = UiState.Success
      }catch (e: IOException) {
          updateState = UiState.Error
      }
    }

    fun updateTimeWatering(newTime: String, day: String) {
        try {
            updateState = UiState.Loading
            sensorRepository.updateWateringSchedule(newTime, day)

            updateState = UiState.Success
        }catch (e: IOException) {
            updateState = UiState.Error
        }
    }



}
// No need to copy over
sealed interface UiState {
    data object Success : UiState
    data object Error : UiState
    data object Loading : UiState
}