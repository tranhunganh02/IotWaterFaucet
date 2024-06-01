package Final.iotwater.present.component

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment

import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition

@Composable
fun LottieAnimationView(gifAnimation: Int, width: Dp = Dp(300f), // Adjust width as needed
                        height: Dp = Dp(305f)) {
    val composition by rememberLottieComposition(spec =  LottieCompositionSpec.RawRes(resId =  gifAnimation))

    LottieAnimation(composition = composition, iterations = LottieConstants.IterateForever, modifier = Modifier
        .size(width, height)) // Set siz

}

