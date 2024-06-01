package Final.iotwater.helper;

import android.annotation.SuppressLint
import android.icu.text.DateFormat
import android.icu.text.SimpleDateFormat
import androidx.compose.runtime.Composable;
import java.util.Date

@SuppressLint("SimpleDateFormat")
@Composable
fun convertStringToDate(dateString: String): Date {
        // Try parsing the String using two common date formats (YYYY-MM-DD'T'HH:mm:ss.SSS'Z' and YYYY-MM-DD HH:mm:ss)
        val formats = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")


        // If parsing fails with both formats, return null
        return formats.parse(dateString)
}

