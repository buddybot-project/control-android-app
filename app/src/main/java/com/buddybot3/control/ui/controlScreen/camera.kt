package com.buddybot3.control.ui.controlScreen

import android.Manifest
import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.FrameLayout
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner

class Camera(private val context: Context) {
    private lateinit var previewView: PreviewView

    @Composable
    fun CameraView(modifier: Modifier = Modifier) {
        AndroidView(
            factory = {
                previewView = PreviewView(context).apply {
                    layoutParams = FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.MATCH_PARENT
                    )
                }
                previewView
            },
            modifier = modifier.fillMaxSize()
        ) {
            startCamera(context as Activity)
        }
    }

    private fun startCamera(activity: Activity) {
        ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.CAMERA), 0)
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build().also {
                it.surfaceProvider = previewView.surfaceProvider
            }

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(context as LifecycleOwner, cameraSelector, preview)
            } catch (e: Exception) {
                Log.e("CameraX", "Error starting camera", e)
            }

        }, ContextCompat.getMainExecutor(context))
    }
}