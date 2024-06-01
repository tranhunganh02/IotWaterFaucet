package Final.iotwater.present.screen

import Final.iotwater.R
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import Final.iotwater.data.WateringSchedule
import Final.iotwater.present.UiState
import Final.iotwater.present.component.LottieAnimationView
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleScreen(
    stateUi: UiState,
    modifier: Modifier = Modifier,
    wateringSchedule: WateringSchedule? = null,
    onBackButtonClicked: () -> Unit = {},
    updateTimeWatering: (String, String) -> Unit
) {
    val daysOfWeek =
        listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")

    var selectedDay by remember { mutableStateOf("") }
    var isSelectDay by remember { mutableStateOf(false) }
    var isTimePickerOpen by remember { mutableStateOf(false) }
    val selectedTime = remember {
       mutableStateOf("")
    }
    val state = rememberTimePickerState()

    val openAlertDialog = remember { mutableStateOf(false) }

    var successAnimationVisibility by remember { mutableStateOf(false) }

    // Define animation specs
    val fadeInOutAlphaSpec = remember {
        fadeIn(
            animationSpec = tween(
                durationMillis = 1000,
                easing = FastOutSlowInEasing
            )
        )
    }

    if (successAnimationVisibility) {
        LaunchedEffect(Unit) {
            successAnimationVisibility = true
            delay(4000) // Adjust the delay as needed
            successAnimationVisibility = false
        }
    }



    Column(
        modifier = modifier.padding(horizontal = 10.dp, vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (stateUi) {
            is UiState.Loading -> CircularProgressIndicator(modifier = Modifier.padding(top = 16.dp))
            is UiState.Success -> {


                DisplayText(daysOfWeek[0], wateringSchedule?.monday ?: "")
                DisplayText(daysOfWeek[1], wateringSchedule?.tuesday ?: "")
                DisplayText(daysOfWeek[2], wateringSchedule?.wednesday ?: "")
                DisplayText(daysOfWeek[3], wateringSchedule?.thursday ?: "")
                DisplayText(daysOfWeek[4], wateringSchedule?.friday ?: "")
                DisplayText(daysOfWeek[5], wateringSchedule?.saturday ?: "")
                DisplayText(daysOfWeek[6], wateringSchedule?.sunday ?: "")


                Row(
                    modifier = Modifier
                        .fillMaxWidth()

                        .height(100.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically

                ) {
                    TextButton(onClick = { isSelectDay = !isSelectDay }) {
                        Text(
                            text = "Choose day to make time watering", modifier = Modifier
                                .align(Alignment.Top)
                        )
                    }
                    DropdownMenu(

                        expanded = isSelectDay,
                        onDismissRequest = { isSelectDay = false }
                    ) {
                        daysOfWeek.forEach { day ->
                            DropdownMenuItem(
                                text = { Text(text = day) },
                                onClick = {
                                    selectedDay = day
                                    isSelectDay = false
                                }
                            )
                        }
                    }
                    Text(
                        text = selectedDay, modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(bottom = 5.dp)
                    )
                }


               Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                   IconButton(
                       onClick = {
                           isTimePickerOpen = true
                       }
                   ) {
                       Icon(
                           imageVector = Icons.Filled.DateRange,
                           contentDescription = "Time Picker"
                       )
                   }

                   if (selectedTime.value != "" ){
                       Text(text = selectedTime.value)
                   }
               }

                if ( openAlertDialog.value ) {

                        AlertDialogMissing(
                            onDismissRequest = { openAlertDialog.value = false },
                            onConfirmation = {
                                openAlertDialog.value = false
                                println("Ok") // Add logic here to handle confirmation.
                            },
                            dialogTitle = "Oops, you missing date",
                            dialogText = "",
                            icon = Icons.Default.Info
                        )

                }

                if (isTimePickerOpen) {
                    TimePickerDialog(
                        onDismissRequest = {

                        },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    val formattedHour = state.hour.toString().padStart(2, '0')
                                    val formattedMinute = state.minute.toString().padStart(2, '0')
                                    selectedTime.value = "$formattedHour:$formattedMinute:00"
                                    isTimePickerOpen = !isTimePickerOpen
                                    Log.d("timeee", "${state.hour}${state.minute}${state} ")

                                }
                            ) { Text("OK") }

                        },
                        dismissButton = {
                            TextButton(
                                onClick = {
                                    isTimePickerOpen =  !isTimePickerOpen
                                }
                            ) { Text("Cancel") }
                        }
                    )
                    {
                        TimePicker(state =  state  )
                    }
                }



                Button(
                    onClick = {
                        if (selectedDay.isNotEmpty() && selectedTime.value.isNotEmpty()) {
                            Log.d("dateee", selectedDay.take(3).lowercase())
                           updateTimeWatering(selectedTime.value, selectedDay.take(3).lowercase())
                        } else {
                            // Hiển thị thông báo nếu selectedDay hoặc selectedTime chưa được chọn
                            openAlertDialog.value = true
                        }

                    },
//                    enabled = selectedDay.isNotEmpty()
                ) {
                    Text("Update Watering Time")
                }
            }

            is UiState.Error -> {
                Text(text = "Error loading watering schedule")
            }


        }
    }

}

@Composable
fun DisplayText(day: String, time: String) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier
            .height(60.dp)
            .fillMaxWidth()
    ) {
        Text(text = day, style = MaterialTheme.typography.bodyLarge)
        Text(
            text = time, style = MaterialTheme.typography.bodyLarge
        )
    }
}
@Composable
fun TimePickerDialog(
    title: String = "Select Time",
    onDismissRequest: () -> Unit,
    confirmButton: @Composable (() -> Unit),
    dismissButton: @Composable (() -> Unit)? = null,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    content: @Composable () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        ),
    ) {
        Surface(
            shape = MaterialTheme.shapes.extraLarge,
            tonalElevation = 6.dp,
            modifier = Modifier
                .width(IntrinsicSize.Min)
                .height(IntrinsicSize.Min)
                .background(
                    shape = MaterialTheme.shapes.extraLarge,
                    color = containerColor
                ),
            color = containerColor
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                    text = title,
                    style = MaterialTheme.typography.labelMedium
                )
                content()
                Row(
                    modifier = Modifier
                        .height(40.dp)
                        .fillMaxWidth()
                ) {
                    Spacer(modifier = Modifier.weight(1f))
                    dismissButton?.invoke()
                    confirmButton()
                }
            }
        }
    }
}

@Composable
fun AlertDialogMissing(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    icon: ImageVector,
) {
    AlertDialog(
        icon = {
            Icon(icon, contentDescription = "Example Icon")
        },
        title = {
            Text(text = dialogTitle)
        },
        text = {
            Text(text = dialogText)
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                }
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text("Dismiss")
            }
        }
    )
}