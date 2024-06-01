package Final.iotwater

import Final.iotwater.present.SensorViewModel
import Final.iotwater.present.screen.HomeScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import Final.iotwater.ui.theme.IotWaterTheme
import SensorWaterApp
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            IotWaterTheme {
                // A surface container using the 'background' color from the theme
                SensorWaterApp()
            }
        }
    }
}
