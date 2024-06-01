package Final.iotwater.repository

import Final.iotwater.data.Humidity
import Final.iotwater.data.WateringSchedule
import Final.iotwater.data.WateringTime
import Final.iotwater.domain.SensorRepository
import Final.iotwater.helper.convertLocalTimeToString
import Final.iotwater.utils.ResultState
import android.content.ContentValues.TAG
import android.util.Log
import androidx.collection.mutableObjectLongMapOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseException
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow

class FirebaseSensorRepository(private val firebaseDatabase: FirebaseDatabase,

): SensorRepository {
    val TAGb = "fbbb"

    private val faucetStateRef = firebaseDatabase.getReference("faucet-state")
    private val humidityRef = firebaseDatabase.getReference("humidity")
    private val wateringScheduleRef = firebaseDatabase.getReference("watering-schedule")

    private val rootRef = firebaseDatabase.reference

    private val daysOfWeek = listOf(
        "mon",
        "tue",
        "wed",
        "thu",
        "fri",
        "sat",
        "sun"
    )

    override fun getFaucetState(): LiveData<Boolean> {
        val faucetStateLiveData = MutableLiveData<Boolean>()

        // Truy vấn giá trị của faucet-state từ root node
        faucetStateRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val faucetState = dataSnapshot.getValue<Boolean>()

                Log.d(TAG, "Value faucetState is: " + faucetState)

                faucetStateLiveData.value = faucetState!!  // Gán giá trị mặc định nếu faucetState null
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Xử lý lỗi
            }
        })

        return faucetStateLiveData
    }

    override fun getHumidity(): MutableLiveData<Int> {
        val humidityLiveData = MutableLiveData<Int>()

        // Truy vấn giá trị của humidity từ root node
        humidityRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val humidity = dataSnapshot.getValue<Int>()
                Log.d(TAG, "Value humidity is: " + humidity)
                humidityLiveData.value = humidity!!
            }


            override fun onCancelled(error: DatabaseError) {
                // Handle errors
                Log.w("FirebaseHumidityRepo", "Error getting humidity data $error")
            }
        })

        return humidityLiveData
    }


    override fun getLastWateringTime(): LiveData<WateringTime> {
        val lastWateringTimeLiveData = MutableLiveData<WateringTime>()

        // Truy vấn giá trị của last-time-watering từ root node
        rootRef.child("last-time-watering").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val lastWateringTime = dataSnapshot.getValue(WateringTime::class.java)
                lastWateringTimeLiveData.value = lastWateringTime!! // Không xử lý null vì WateringTime có thể null
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Xử lý lỗi
            }
        })

        return lastWateringTimeLiveData
    }

    override fun getWateringSchedule(): LiveData<WateringSchedule> {
        val dailySchedules = mutableMapOf<String, String>()

        val _wateringScheduleLiveData = MutableLiveData<WateringSchedule>() // Declare private MutableLiveData

        wateringScheduleRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val wateringSchedule: WateringSchedule

                if (dataSnapshot.exists()) {
                    for (childSnapshot in dataSnapshot.children) {
                        val day = childSnapshot.key ?: ""
                        val value = childSnapshot.value?.toString() ?: ""
                        dailySchedules[day] = convertLocalTimeToString(dateTime = value)
                    }

                    // Construct WateringSchedule object
                    wateringSchedule = WateringSchedule(
                        dailySchedules["mon"] ?: "",
                        dailySchedules["tue"] ?: "",
                        dailySchedules["wed"] ?: "",
                        dailySchedules["thu"] ?: "",
                        dailySchedules["fri"] ?: "",
                        dailySchedules["sat"] ?: "",
                        dailySchedules["sun"] ?: ""
                    )
                } else {
                    // Handle case where data doesn't exist
                    wateringSchedule = WateringSchedule("", "", "", "", "", "", "") // Create empty schedule
                }

                // Update LiveData with the constructed WateringSchedule object
                _wateringScheduleLiveData.value = wateringSchedule
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle errors
                Log.w("FirebaseHumidityRepo", "Error getting humidity data $error")
            }
        })

        // Return the LiveData with the updated WateringSchedule
        return _wateringScheduleLiveData
    }




    override fun updateWaterPumpState(state: Boolean) {
        faucetStateRef.setValue(state)
    }

    override fun updateFaucetState(newState: Boolean) {
        faucetStateRef.setValue(newState)
            .addOnSuccessListener {
                // Xử lý thành công nếu cần
            }
            .addOnFailureListener { e ->
                // Xử lý khi thất bại
                Log.e("FirebaseSensorRepo", "Error updating faucet state", e)
            }
    }

    override fun updateWateringSchedule(newTime: String, day: String) {
        wateringScheduleRef.child(day).setValue(newTime)
    }

}