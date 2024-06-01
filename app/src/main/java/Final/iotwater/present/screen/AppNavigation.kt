import Final.iotwater.R
import Final.iotwater.present.SensorViewModel
import Final.iotwater.present.screen.HomeScreen
import Final.iotwater.present.screen.ScheduleScreen
import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue

import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

enum class AppScreen(@StringRes val title: Int) {
    Home(title = R.string.app_name),
    Schedule(title = R.string.schedule),
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    currentScreen: AppScreen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    CenterAlignedTopAppBar(
        title = {
             // Ensures full width for centering
                Text(
                    style = MaterialTheme.typography.headlineLarge,
                    text = stringResource(currentScreen.title),
                   // Center the text

                )

        },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier,
        navigationIcon = {

            if (canNavigateBack) {

                    IconButton(onClick = navigateUp,) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back_button)
                        )
                    }

            }
        }
    )
}


@Composable
fun SensorWaterApp(
    navController: NavHostController = rememberNavController()

) {
    val sensorViewModel: SensorViewModel = viewModel()
    // Get current back stack entry
    val backStackEntry by navController.currentBackStackEntryAsState()
    // Get the name of the current screen
    val currentScreen = AppScreen.valueOf(
        backStackEntry?.destination?.route ?: AppScreen.Home.name
    )



    Scaffold(
        topBar = {
            AppBar(
                currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() },

            )
        }
    ) { innerPadding ->

        val stateButton = sensorViewModel.updateState
        val stateUi = sensorViewModel.uiState
        val faucetState by sensorViewModel.faucetState.observeAsState()
        val humidity by sensorViewModel.humidity.observeAsState()
        val lastWateringTime by sensorViewModel.lastWateringTime.observeAsState()

        val wateringSchedule by sensorViewModel.wateringSchedule.observeAsState()

        NavHost(
            navController = navController,
            startDestination = AppScreen.Home.name,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
        ) {

            composable(route = AppScreen.Home.name) {
                print("app screen : $humidity")
                Log.d("fbbb", "SensorWaterApp appscreen : $humidity")
                lastWateringTime?.let { it1 ->
                    HomeScreen(
                        stateUi = stateUi,
                        stateButton = stateButton,
                        faucetState = faucetState ?: false,
                        humidity = humidity ?: 0,
                        lastWateringTime = it1,
                        modifier = Modifier
                            .fillMaxSize(),
                        updateFaucetState = { newState -> sensorViewModel.updateFaucetState(newState) },
                        onNextButtonClicked = { navController.navigate(AppScreen.Schedule.name) }
                    )
                }
            }
            composable(route = AppScreen.Schedule.name) {
                wateringSchedule?.let { it1 ->
                    ScheduleScreen(
                        stateUi = stateUi,
                        modifier = Modifier
                            .fillMaxSize(),
                        wateringSchedule = wateringSchedule!!,
                        onBackButtonClicked = { navController.navigate(AppScreen.Schedule.name) },
                        updateTimeWatering = { newTime, day ->
                            sensorViewModel.updateTimeWatering(
                                newTime,
                                day
                            )
                        }
                    )
                } ?: run {
                    // Handle the case where wateringSchedule is null (loading or error)
                    Text(text = "Loading schedule...")
                }
            }

        }
    }
}
