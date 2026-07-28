package com.ecommerce.ecommerceposapp.presentation.pos

import android.Manifest
import android.content.pm.PackageManager
import android.hardware.camera2.CaptureRequest
import android.util.Size
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.camera2.interop.Camera2Interop
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size as ComposeSize
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.ContextCompat
import com.ecommerce.ecommerceposapp.ui.theme.BrandRed
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicBoolean

@Composable
internal fun CameraScannerDialog(
    onBarcodeDetected: (String) -> Unit,
    onDismiss: () -> Unit,
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED,
        )
    }
    var cameraProvider by remember { mutableStateOf<ProcessCameraProvider?>(null) }
    val processed = remember { AtomicBoolean(false) }
    val analyzerExecutor = remember { Executors.newSingleThreadExecutor() }
    val scanner = remember {
        BarcodeScanning.getClient(
            BarcodeScannerOptions.Builder()
                .setBarcodeFormats(
                    Barcode.FORMAT_EAN_13,
                    Barcode.FORMAT_EAN_8,
                    Barcode.FORMAT_UPC_A,
                    Barcode.FORMAT_UPC_E,
                    Barcode.FORMAT_CODE_128,
                )
                .build(),
        )
    }
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { granted -> hasCameraPermission = granted }

    LaunchedEffect(Unit) {
        if (!hasCameraPermission) permissionLauncher.launch(Manifest.permission.CAMERA)
    }
    DisposableEffect(Unit) {
        onDispose {
            cameraProvider?.unbindAll()
            analyzerExecutor.shutdown()
            scanner.close()
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .widthIn(max = 620.dp)
                .height(540.dp),
            shape = RoundedCornerShape(8.dp),
            color = Color.Black,
            shadowElevation = 16.dp,
        ) {
            Box(Modifier.fillMaxSize()) {
                if (hasCameraPermission) {
                    AndroidView(
                        modifier = Modifier.fillMaxSize(),
                        factory = { ctx ->
                            PreviewView(ctx).apply {
                                scaleType = PreviewView.ScaleType.FILL_CENTER
                                implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                                val providerFuture = ProcessCameraProvider.getInstance(ctx)
                                providerFuture.addListener({
                                    val provider = providerFuture.get()
                                    cameraProvider = provider
                                    val previewBuilder = Preview.Builder()
                                    Camera2Interop.Extender(previewBuilder).setCaptureRequestOption(
                                        CaptureRequest.CONTROL_AF_MODE,
                                        CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE,
                                    )
                                    val preview = previewBuilder.build().also {
                                        it.setSurfaceProvider(surfaceProvider)
                                    }
                                    val analysis = ImageAnalysis.Builder()
                                        .setTargetResolution(Size(1280, 720))
                                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                                        .build()
                                    analysis.setAnalyzer(analyzerExecutor) { imageProxy ->
                                        val mediaImage = imageProxy.image
                                        if (mediaImage == null || processed.get()) {
                                            imageProxy.close()
                                            return@setAnalyzer
                                        }
                                        scanner.process(
                                            InputImage.fromMediaImage(
                                                mediaImage,
                                                imageProxy.imageInfo.rotationDegrees,
                                            ),
                                        ).addOnSuccessListener { barcodes ->
                                            val code = barcodes.firstOrNull { !it.rawValue.isNullOrBlank() }?.rawValue
                                            if (!code.isNullOrBlank() && processed.compareAndSet(false, true)) {
                                                analysis.clearAnalyzer()
                                                provider.unbindAll()
                                                onBarcodeDetected(code)
                                            }
                                        }.addOnCompleteListener {
                                            imageProxy.close()
                                        }
                                    }
                                    provider.unbindAll()
                                    provider.bindToLifecycle(
                                        lifecycleOwner,
                                        CameraSelector.DEFAULT_BACK_CAMERA,
                                        preview,
                                        analysis,
                                    )
                                }, ContextCompat.getMainExecutor(ctx))
                            }
                        },
                    )
                    ScannerOverlay()
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFF111827))
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        Icon(
                            Icons.Filled.QrCodeScanner,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(54.dp),
                        )
                        Text(
                            "Permite el acceso a la cámara para escanear productos.",
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(top = 16.dp),
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .fillMaxWidth()
                        .background(Color.Black.copy(alpha = 0.62f))
                        .padding(horizontal = 56.dp, vertical = 14.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        "Escanear producto",
                        color = Color.White,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        "Centra el código de barras dentro del marco",
                        color = Color.White.copy(alpha = 0.82f),
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.Center,
                    )
                }
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(10.dp)
                        .size(38.dp)
                        .background(Color.Black.copy(alpha = 0.55f), CircleShape),
                ) {
                    Icon(Icons.Filled.Close, contentDescription = "Cerrar escáner", tint = Color.White)
                }
            }
        }
    }
}

@Composable
private fun ScannerOverlay() {
    val transition = rememberInfiniteTransition(label = "scanner")
    val progress by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1_350),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "scanLine",
    )
    Canvas(Modifier.fillMaxSize()) {
        val frameWidth = size.width * 0.82f
        val frameHeight = size.height * 0.34f
        val left = (size.width - frameWidth) / 2f
        val top = (size.height - frameHeight) / 2f + 20.dp.toPx()
        val right = left + frameWidth
        val bottom = top + frameHeight
        val mask = Color.Black.copy(alpha = 0.58f)

        drawRect(mask, size = ComposeSize(size.width, top))
        drawRect(mask, topLeft = Offset(0f, bottom), size = ComposeSize(size.width, size.height - bottom))
        drawRect(mask, topLeft = Offset(0f, top), size = ComposeSize(left, frameHeight))
        drawRect(mask, topLeft = Offset(right, top), size = ComposeSize(size.width - right, frameHeight))

        drawRoundRect(
            color = Color.White.copy(alpha = 0.30f),
            topLeft = Offset(left, top),
            size = ComposeSize(frameWidth, frameHeight),
            style = Stroke(width = 1.dp.toPx()),
        )
        val corner = 28.dp.toPx()
        val stroke = 4.dp.toPx()
        fun line(start: Offset, end: Offset) =
            drawLine(BrandRed, start, end, strokeWidth = stroke, cap = StrokeCap.Round)
        line(Offset(left, top + corner), Offset(left, top))
        line(Offset(left, top), Offset(left + corner, top))
        line(Offset(right - corner, top), Offset(right, top))
        line(Offset(right, top), Offset(right, top + corner))
        line(Offset(left, bottom - corner), Offset(left, bottom))
        line(Offset(left, bottom), Offset(left + corner, bottom))
        line(Offset(right - corner, bottom), Offset(right, bottom))
        line(Offset(right, bottom), Offset(right, bottom - corner))

        val scanY = top + 14.dp.toPx() + progress * (frameHeight - 28.dp.toPx())
        drawLine(
            color = BrandRed.copy(alpha = 0.28f),
            start = Offset(left + 12.dp.toPx(), scanY),
            end = Offset(right - 12.dp.toPx(), scanY),
            strokeWidth = 10.dp.toPx(),
            cap = StrokeCap.Round,
        )
        drawLine(
            color = BrandRed,
            start = Offset(left + 12.dp.toPx(), scanY),
            end = Offset(right - 12.dp.toPx(), scanY),
            strokeWidth = 2.dp.toPx(),
            cap = StrokeCap.Round,
        )
    }
}
