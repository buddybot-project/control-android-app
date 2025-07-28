package com.buddybot3.control

import android.os.Bundle
import androidx.activity.ComponentActivity
import com.buddybot3.control.ui.CreateUI
import androidx.activity.compose.setContent
import com.buddybot3.control.ui.setupUI

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupUI(window)

        setContent {
            CreateUI()
        }
    }
}