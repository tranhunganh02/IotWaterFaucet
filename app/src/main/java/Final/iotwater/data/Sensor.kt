package Final.iotwater.data

import java.time.LocalDateTime

class Sensor(
    val faucet_state: FaucetState,
    val humidity: Int,
    val lastWateringTime: WateringTime,
    val wateringSchedule: WateringSchedule
)
enum class FaucetState {
    ON,
    OFF
}

data class Humidity(
    val humidity: Int?
)

data class WateringTime(
    val timeWatering: Long,
    val time: String
)
{
    constructor() : this(0, "")
}

data class WateringSchedule(
    val monday: String,
    val tuesday: String,
    val wednesday: String,
    val thursday: String,
    val friday: String,
    val saturday: String,
    val sunday: String
)
