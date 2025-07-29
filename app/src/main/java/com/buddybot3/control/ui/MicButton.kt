package com.buddybot3.control.ui


import android.app.Activity
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.content.pm.PackageManager
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.buddybot3.control.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun MicButton(
    modifier: Modifier = Modifier,
    colorBack: Color=Color(0x3B000000),
    colorFront: Color=Color.White,
    colorActive: Color=Color(0xFF2D9612),
    onClick: (Boolean) -> Unit
) {
    var isActive by remember { mutableStateOf(false) }
    val activity = LocalActivity.current ?: return
    var audioRecord: AudioRecord? by remember { mutableStateOf(null) }
    val coroutineScope = rememberCoroutineScope()

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        isActive = isGranted
        if (isActive) {
            startRecording(activity, coroutineScope) { record ->
                audioRecord = record
            }
        } else {
            audioRecord?.stop()
            audioRecord?.release()
            audioRecord = null
        }
        onClick(isActive)
    }

    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(colorBack)
            .clickable {

                if (ContextCompat.checkSelfPermission(activity, android.Manifest.permission.RECORD_AUDIO)
                    == PackageManager.PERMISSION_GRANTED) {
                    isActive = !isActive
                    if (isActive) {
                        startRecording(activity, coroutineScope) { record ->
                            audioRecord = record
                        }
                    } else {
                        audioRecord?.stop()
                        audioRecord?.release()
                        audioRecord = null
                    }
                    onClick(isActive)
                } else {
                    permissionLauncher.launch(android.Manifest.permission.RECORD_AUDIO)
                }
            }
    ) {
        Image(
            painter = painterResource(id = R.drawable.microphone),
            contentDescription = "micro",
            modifier = Modifier.padding(7.dp).fillMaxSize(),
            contentScale = ContentScale.Fit,
            colorFilter = ColorFilter.tint(if (isActive) colorActive else colorFront),
        )
    }
}

private fun startRecording(
    activity: Activity,
    coroutineScope: CoroutineScope,
    onReady: (AudioRecord) -> Unit
) {
    val sampleRate = 44100
    val bufferSize = AudioRecord.getMinBufferSize(
        sampleRate,
        AudioFormat.CHANNEL_IN_MONO,
        AudioFormat.ENCODING_PCM_16BIT
    )

    if (ContextCompat.checkSelfPermission(activity, android.Manifest.permission.RECORD_AUDIO)
        == PackageManager.PERMISSION_GRANTED) {
        val audioRecord = AudioRecord(
            MediaRecorder.AudioSource.MIC,
            sampleRate,
            AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT,
            bufferSize
        )

        audioRecord.startRecording()
        onReady(audioRecord)

        coroutineScope.launch(Dispatchers.IO) {
            val buffer = ByteArray(bufferSize)
            while (audioRecord.recordingState == AudioRecord.RECORDSTATE_RECORDING) {
                audioRecord.read(buffer, 0, buffer.size)
                // Здесь мы просто читаем звук и ничего не сохраняем
            }
        }
    }
}