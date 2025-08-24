package com.buddybot3.control

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.buddybot3.control.ui.controlScreen.ControlScreenActivity

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startActivity(Intent(this, ControlScreenActivity::class.java))

    }
}