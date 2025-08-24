package com.buddybot3.control.ui.controlScreen

import android.util.Log
import android.view.Window
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import kotlin.math.min
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

class ControlScreenActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupUI(window)

        setContent {
            CreateUI()
        }
    }
}

fun setupUI(window: Window) {
    // Установка горизонтального режима и скрытие лишних элементов
    WindowCompat.setDecorFitsSystemWindows(window, false)
    val controller = WindowInsetsControllerCompat(window, window.decorView)
    controller.hide(WindowInsetsCompat.Type.systemBars())
    controller.systemBarsBehavior =
        WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
}

@Composable
fun CreateUI() {
    Box(modifier = Modifier.fillMaxSize()) {
        Camera(LocalContext.current).CameraView()
        val joySize = min(LocalWindowInfo.current.containerSize.width * 0.15f, LocalWindowInfo.current.containerSize.height * 0.2f)
        JoyStick(
            modifier = Modifier
                .padding(25.dp)
                .size(joySize.dp)
                .align(Alignment.BottomEnd),
            dotSize = 30.dp
        ) { x: Float, y: Float ->
            Log.d("Joystick right", "x=$x, y=$y")
        }
        JoyStick(
            modifier = Modifier
                .padding(25.dp)
                .size(joySize.dp)
                .align(Alignment.BottomStart),
            dotSize = 30.dp
        ) { x: Float, y: Float ->
            Log.d("Joystick left", "x=$x, y=$y")
        }
        Column(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(end = 5.dp, top = 5.dp, bottom = 5.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            MicButton(
                modifier = Modifier.size(45.dp).fillMaxSize()
            ) { i: Boolean ->
                Log.d("MicButton", "Clicked($i)")
            }
        }
    }
}
