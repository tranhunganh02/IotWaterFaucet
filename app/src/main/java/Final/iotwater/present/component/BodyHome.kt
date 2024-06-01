package Final.iotwater.present.component

import Final.iotwater.data.WateringTime
import Final.iotwater.present.UiState
import android.annotation.SuppressLint
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDateTime

import java.time.format.DateTimeFormatter

@SuppressLint("SuspiciousIndentation")
@Composable
fun BodyHome(
    faucetState: Boolean,
    humidity: Int,
    lastWateringTime: WateringTime,
    updateFaucetState: (Boolean) -> Unit,
    stateButton: UiState,
    onNextButtonClicked: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(5.dp)
            .background(Color.White),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Hiển thị độ ẩm
        val weatherCondition = findWeatherCondition(humidity)


        Column(
            modifier = Modifier
                .weight(2f)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Độ ẩm hiện tại ${humidity} W",
                fontSize = 40.sp
            )
            Text(
                text = stringResource(weatherCondition.effectPlants),
                fontSize = 15.sp
            )
            LottieAnimationView(weatherCondition.gifAnimation)
            Spacer(modifier = Modifier.height(20.dp))


        }

        Button(
            modifier = Modifier.width(150.dp),
            onClick = {
                updateFaucetState(!faucetState)

            },
            colors = ButtonDefaults.buttonColors(containerColor = weatherCondition.colorBg)
        ) {

            Text(text = if (faucetState) "Tắt Vòi" else "Bật Vòi")
        }

        // Parse the string to LocalDateTim

        val formattedDateTime = lastWateringTime.time.replace("T", " ").replace("Z", "")


        Box(Modifier.weight(1f)) {

            Column(Modifier.fillMaxSize(),horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Bottom) {
                Text(
                    text = "Lần tưới gần đây nhất: $formattedDateTime ",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .padding(8.dp)

                )
                Spacer(modifier = Modifier.height(10.dp))
                Button(
                    modifier = Modifier.width(250.dp),
                    onClick = { onNextButtonClicked() },
                    colors = ButtonDefaults.buttonColors()
                ) {

                    Text(text = "Đặt lịch tưới")
                }
            }
        }
    }
}

