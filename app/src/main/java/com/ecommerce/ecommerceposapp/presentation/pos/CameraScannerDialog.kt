package com.ecommerce.ecommerceposapp.presentation.pos

import android.Manifest
import android.content.pm.PackageManager
import android.hardware.camera2.CaptureRequest
import android.util.Size
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.camera2.interop.Camera2Interop
import androidx.camera.core.Camera
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
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FlashlightOff
import androidx.compose.material.icons.filled.FlashlightOn
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
    var camera by remember { mutableStateOf<Camera?>(null) }
    var torchEnabled by remember { mutableStateOf(false) }
    var flashAvailable by remember { mutableStateOf(false) }
    val processed = remember { AtomicBoolean(false) }
    val analyzing = remember { AtomicBoolean(false) }
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
                    Barcode.FORMAT_QR_CODE,
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
            camera?.cameraControl?.enableTorch(false)
            cameraProvider?.unbindAll()
            analyzerExecutor.shutdown()
            scanner.close()
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false,
        ),
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.Black,
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
                                        .setTargetResolution(Size(960, 540))
                                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                                        .build()
                                    analysis.setAnalyzer(analyzerExecutor) { imageProxy ->
                                        val mediaImage = imageProxy.image
                                        if (
                                            mediaImage == null ||
                                            processed.get() ||
                                            !analyzing.compareAndSet(false, true)
                                        ) {
                                            imageProxy.close()
                                            return@setAnalyzer
                                        }
                                        scanner.process(
                                            InputImage.fromMediaImage(
                                                mediaImage,
                                                imageProxy.imageInfo.rotationDegrees,
                                            ),
                                        ).addOnSuccessListener { barcodes ->
                                            val code = barcodes.firstOrNull {
                                                !it.rawValue.isNullOrBlank()
                                            }?.rawValue
                                            if (!code.isNullOrBlank() &&
                                                processed.compareAndSet(false, true)
                                            ) {
                                                analysis.clearAnalyzer()
                                                provider.unbindAll()
                                                onBarcodeDetected(code)
                                            }
                                        }.addOnCompleteListener {
                                            analyzing.set(false)
                                            imageProxy.close()
                                        }
                                    }
                                    provider.unbindAll()
                                    camera = provider.bindToLifecycle(
                                        lifecycleOwner,
                                        CameraSelector.DEFAULT_BACK_CAMERA,
                                        preview,
                                        analysis,
                                    ).also {
                                        flashAvailable = it.cameraInfo.hasFlashUnit()
                                    }
                                }, ContextCompat.getMainExecutor(ctx))
                            }
                        },
                    )
                    ScannerOverlay()
                } else {
                    PermissionMessage()
                }

                Row(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .fillMaxWidth()
                        .background(Color.Black.copy(alpha = 0.42f))
                        .padding(start = 24.dp, end = 12.dp, top = 18.dp, bottom = 14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        "Escanear código",
                        color = Color.White,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f),
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(
                            Icons.Filled.Close,
                            contentDescription = "Cerrar escáner",
                            tint = Color.White,
                            modifier = Modifier.size(32.dp),
                        )
                    }
                }

                if (hasCameraPermission) {
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 50.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Row(
                            modifier = Modifier
                                .clickable(enabled = flashAvailable) {
                                    val next = !torchEnabled
                                    camera?.cameraControl?.enableTorch(next)
                                    torchEnabled = next
                                }
                                .padding(horizontal = 20.dp, vertical = 14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(
                                if (torchEnabled) Icons.Filled.FlashlightOn
                                else Icons.Filled.FlashlightOff,
                                contentDescription = null,
                                tint = if (flashAvailable) Color.White else Color.White.copy(alpha = 0.45f),
                                modifier = Modifier.size(28.dp),
                            )
                            Spacer(Modifier.width(12.dp))
                            Text(
                                when {
                                    !flashAvailable -> "Linterna no disponible"
                                    torchEnabled -> "Apagar la linterna"
                                    else -> "Encender la linterna"
                                },
                                color = if (flashAvailable) Color.White else Color.White.copy(alpha = 0.45f),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold,
                            )
                        }
                        Spacer(Modifier.height(24.dp))
                        Text(
                            "Centra el código de barras o QR dentro del marco",
                            color = Color.White.copy(alpha = 0.88f),
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(
                                    width = 1.5.dp,
                                    color = Color.White,
                                    shape = RoundedCornerShape(28.dp),
                                )
                                .padding(horizontal = 20.dp, vertical = 15.dp),
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PermissionMessage() {
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
        val frameSize = minOf(size.width * 0.58f, 310.dp.toPx())
        val left = (size.width - frameSize) / 2f
        val top = (size.height - frameSize) / 2f - 35.dp.toPx()
        val right = left + frameSize
        val bottom = top + frameSize
        val mask = Color.Black.copy(alpha = 0.48f)

        drawRect(mask, size = ComposeSize(size.width, top))
        drawRect(mask, topLeft = Offset(0f, bottom), size = ComposeSize(size.width, size.height - bottom))
        drawRect(mask, topLeft = Offset(0f, top), size = ComposeSize(left, frameSize))
        drawRect(mask, topLeft = Offset(right, top), size = ComposeSize(size.width - right, frameSize))
        drawRoundRect(
            color = Color.White,
            topLeft = Offset(left, top),
            size = ComposeSize(frameSize, frameSize),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(18.dp.toPx()),
            style = Stroke(width = 3.dp.toPx()),
        )
        val scanY = top + 16.dp.toPx() + progress * (frameSize - 32.dp.toPx())
        drawLine(
            color = Color(0xFF32C400),
            start = Offset(left - 14.dp.toPx(), scanY),
            end = Offset(right + 14.dp.toPx(), scanY),
            strokeWidth = 3.dp.toPx(),
            cap = StrokeCap.Round,
        )
    }
}
