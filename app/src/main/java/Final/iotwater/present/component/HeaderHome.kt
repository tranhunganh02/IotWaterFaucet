package Final.iotwater.present.component

import Final.iotwater.R
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp

enum class WeatherCondition(val range: IntRange, val description: String, val effectPlants: Int, val colorBg: Color, val gifAnimation: Int) {
    VERY_DRY(0 until 20, "Khô hạn", (R.string.effect_on_plant_1),  Color(0XFFF44336), R.raw.warning_red),
    DRY(20 until 40, "Khô", R.string.effect_on_plant_2,  Color(0xFFFAA519),  R.raw.warning_orange),
    MOIST(40 until 60, "Thích hợp", R.string.effect_on_plant_3,  Color(0xFF33B5E5), R.raw.tree),
    WET(60 until 80, "Ẩm Ướt", R.string.effect_on_plant_4, Color(0xFFAA66CC),  R.raw.warning_orange),
    VERY_WET(80..100, "Ngập úng", R.string.effect_on_plant_5, Color(0xFF99CC00),  R.raw.warning_red)
}


fun findWeatherCondition(humidityPercentage: Int): WeatherCondition {
    return WeatherCondition.entries.firstOrNull { condition ->
        humidityPercentage in condition.range
    } ?: WeatherCondition.VERY_DRY // Default to Very Dry if not found
}
