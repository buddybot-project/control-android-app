package com.buddybot3.control.ui.controlScreen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.min

@Composable
fun JoyStick(
    modifier: Modifier = Modifier,
    dotSize: Dp = 30.dp,
    colorBack: Color=Color(0x3B000000),
    colorDot: Color=Color.White,
    onMove: (Float, Float) -> Unit
) {
    var dragPosition by remember { mutableStateOf(Offset.Zero) }

    Box(
        modifier = modifier
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = {
                        dragPosition = it
                    },
                    onDrag = { change, _ ->
                        val radius = min(size.width, size.height) / 2f
                        val offsetFromCenter = change.position - Offset(radius, radius)
                        val distance = offsetFromCenter.getDistance()

                        dragPosition = if (distance <= radius) {
                            change.position
                        } else {
                            Offset(radius, radius) + offsetFromCenter / distance * radius
                        }

                        val normX = (dragPosition.x - radius) / radius
                        val normY = (dragPosition.y - radius) / radius
                        onMove(normX, normY)
                    },
                    onDragEnd = {
                        dragPosition = Offset(min(size.width, size.height) / 2f, min(size.width, size.height) / 2f)
                        onMove(0f, 0f)
                    }
                )
            }
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(color = colorBack, radius = size.minDimension / 2)

            if (dragPosition == Offset.Zero) {
                dragPosition = Offset(size.width / 2, size.height / 2)
            }

            drawCircle(
                color = colorDot,
                radius = dotSize.toPx() / 2,
                center = dragPosition
            )
        }
    }
}
