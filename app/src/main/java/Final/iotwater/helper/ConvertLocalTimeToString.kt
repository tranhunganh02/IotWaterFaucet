package Final.iotwater.helper

import java.time.LocalTime
import java.time.format.DateTimeFormatter

fun convertLocalTimeToString(dateTime: String): String {
    // Tạo DateTimeFormatter cho đầu vào có định dạng "HH:mm:ss.SSSSSSSSS"
    val formatter = DateTimeFormatter.ofPattern("HH:mm:ss")
    // Parse chuỗi đầu vào thành LocalTime
    val localTime = LocalTime.parse(dateTime, formatter)

    // Định dạng lại LocalTime theo yêu cầu
    val displayFormatter = DateTimeFormatter.ofPattern("hh:mma")
    // Trả về chuỗi được định dạng
    return displayFormatter.format(localTime)
}
