package Final.iotwater.present.screen

import Final.iotwater.R
import Final.iotwater.data.WateringTime
import Final.iotwater.present.UiState
import Final.iotwater.present.component.BodyHome
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(
    stateUi: UiState,
    stateButton: UiState,
    faucetState: Boolean,
    humidity: Int,
    lastWateringTime: WateringTime,
    modifier: Modifier = Modifier,
    updateFaucetState: (Boolean) -> Unit,
    onNextButtonClicked: () -> Unit,
) {
    when(stateUi) {
        is UiState.Loading -> LoadingScreen(modifier = modifier.fillMaxSize())
        is UiState.Success ->  BodyHome(updateFaucetState= updateFaucetState, stateButton = stateButton,  faucetState = faucetState, humidity = humidity, lastWateringTime = lastWateringTime, onNextButtonClicked = onNextButtonClicked)
        is UiState.Error -> ErrorScreen(modifier = modifier.fillMaxSize())

    }

}

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Image(
        modifier = Modifier.size(150.dp),
        painter = painterResource(R.drawable.loading),
        contentDescription = "Loading",
    )
}

@Composable
fun ErrorScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(text = stringResource(R.string.loading_failed), modifier = Modifier.padding(16.dp))
    }
}