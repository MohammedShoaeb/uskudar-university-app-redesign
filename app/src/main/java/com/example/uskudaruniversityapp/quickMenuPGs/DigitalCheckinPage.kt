package com.example.uskudaruniversityapp.quickMenuPGs

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun DummyQrCode(modifier: Modifier = Modifier, isLoading: Boolean = false, isExpired: Boolean = false) {
   val colorScheme= MaterialTheme.colorScheme
    Box(
        modifier = modifier
            .size(200.dp)
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.size(60.dp))
        } else if (isExpired) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    Icons.Default.Error,
                    contentDescription = "QR Code Expired",
                    modifier = Modifier.size(60.dp),
                    tint = MaterialTheme.colorScheme.error
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    "Expired",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.error
                )
            }
        } else {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val cellSize = size.minDimension / 4
                drawRect(colorScheme.onSurface, size = Size(cellSize, cellSize))
                drawRect(colorScheme.onSurface, topLeft = Offset(cellSize * 3, 0f), size = Size(cellSize, cellSize))
                drawRect(colorScheme.onSurface, topLeft = Offset(0f, cellSize * 3), size = Size(cellSize, cellSize))
                drawRect(colorScheme.onSurface, topLeft = Offset(cellSize * 3, cellSize * 3), size = Size(cellSize, cellSize))
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DigitalCheckinPage() {
    var qrCodeValue by remember { mutableStateOf<String?>(null) }
    var timeLeft by remember { mutableStateOf(0) }
    var isGeneratingQr by remember { mutableStateOf(false) }
    var isQrExpired by remember { mutableStateOf(false) }
val schema= MaterialTheme.colorScheme
    val coroutineScope = rememberCoroutineScope()

    val timerProgress by animateFloatAsState(
        targetValue = if (timeLeft > 0) timeLeft / 15f else 0f,
        animationSpec = tween(durationMillis = 1000, easing = LinearEasing),
        label = "timerProgressAnimation"
    )

    LaunchedEffect(qrCodeValue) {
        if (qrCodeValue != null && !isGeneratingQr && !isQrExpired) {
            while (timeLeft > 0) {
                delay(1000L)
                timeLeft--
            }
            if (timeLeft <= 0) {
                isQrExpired = true
                qrCodeValue = null
            }
        }
    }

    val generateQrCode: () -> Unit = {
        isGeneratingQr = true
        isQrExpired = false
        timeLeft = 0
        qrCodeValue = null

        coroutineScope.launch {
            delay(1500L)
            qrCodeValue = "STUDENT_ID_ABC_TIMESTAMP_${System.currentTimeMillis()}"
            timeLeft = 15
            isGeneratingQr = false
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 24.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "MUHAMMED SUAYB",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "990000000000",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
                )
            }

            Spacer(Modifier.height(48.dp))

            Box(
                modifier = Modifier
                    .size(250.dp)
                    .background(Color.Transparent),
                contentAlignment = Alignment.Center
            ) {
                if (qrCodeValue != null && !isGeneratingQr && !isQrExpired) {
                    DummyQrCode(
                        modifier = Modifier.fillMaxSize()
                    )

                    Canvas(Modifier.size(100.dp)) {
                        val strokeWidth = 8.dp.toPx()
                        drawArc(
                            color = schema.primary.copy(alpha = 0.3f),
                            startAngle = 0f,
                            sweepAngle = 360f,
                            useCenter = false,
                            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                        )
                        drawArc(
                            color = when {
                                timeLeft <= 5 ->schema.error
                                timeLeft <= 10 -> schema.tertiary
                                else -> schema.primary
                            },
                            startAngle = -90f,
                            sweepAngle = 360f * timerProgress,
                            useCenter = false,
                            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                        )
                    }
                    Text(
                        text = timeLeft.toString(),
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                } else if (isGeneratingQr) {

                    DummyQrCode(modifier = Modifier.fillMaxSize(), isLoading = true)
                } else if (isQrExpired) {

                    DummyQrCode(modifier = Modifier.fillMaxSize(), isExpired = true)
                } else {

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Default.QrCode,
                            contentDescription = "Tap to generate QR Code",
                            modifier = Modifier.size(100.dp),
                            tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                        )
                        Spacer(Modifier.height(16.dp))
                        Text(
                            "Tap to generate your QR code.",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onBackground,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            Spacer(Modifier.height(48.dp))

            if (isGeneratingQr) {
                Text(
                    text = "Generating QR code...",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center
                )
            } else if (qrCodeValue != null && !isQrExpired) {
                Text(
                    text = "Please scan the generated QR code by holding it in front of the QR code reader on the turnstile.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                    textAlign = TextAlign.Center
                )
            } else {
                Button(
                    onClick = generateQrCode,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (isQrExpired) {
                        Icon(Icons.Default.Refresh, contentDescription = "Regenerate QR code")
                        Spacer(Modifier.width(8.dp))
                        Text("Regenerate QR Code")
                    } else {
                        Icon(Icons.Default.QrCode, contentDescription = "Generate QR code")
                        Spacer(Modifier.width(8.dp))
                        Text("Generate QR Code")
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true, widthDp = 360)
@Composable
fun PreviewDigitalCheckinPage() {
    MaterialTheme {
        DigitalCheckinPage()
    }
}
