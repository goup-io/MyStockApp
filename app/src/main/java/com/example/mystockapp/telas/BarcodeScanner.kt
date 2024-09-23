package com.example.mystockapp.telas

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.util.Size
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.LifecycleOwner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.Executors

@Composable
fun BarcodeScannerView(
    onScanned: (String) -> Unit, // Função para retornar o valor escaneado
    onError: (String) -> Unit // Função para lidar com erros
) {
    val context = LocalContext.current
    val previewView = remember { PreviewView(context) }

    LaunchedEffect(Unit) {
        startCamera(context, previewView, onScanned, onError)
    }

    AndroidView(factory = { previewView })
}

@SuppressLint("UnsafeOptInUsageError")
private suspend fun startCamera(
    context: Context,
    previewView: PreviewView,
    onScanned: (String) -> Unit,
    onError: (String) -> Unit
) {
    withContext(Dispatchers.Main) {
        try {
            val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
            val cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }

            val barcodeOptions = BarcodeScannerOptions.Builder()
                .setBarcodeFormats(
                    Barcode.FORMAT_CODE_128,
                    Barcode.FORMAT_CODE_39,
                    Barcode.FORMAT_CODE_93,
                    Barcode.FORMAT_EAN_8,
                    Barcode.FORMAT_EAN_13,
                    Barcode.FORMAT_UPC_A,
                    Barcode.FORMAT_UPC_E,
                    Barcode.FORMAT_QR_CODE,
                    Barcode.FORMAT_DATA_MATRIX,
                    Barcode.FORMAT_PDF417,
                    Barcode.FORMAT_AZTEC
                )
                .build()

            val barcodeScanner = BarcodeScanning.getClient(barcodeOptions)
            val textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

            val imageAnalyzer = ImageAnalysis.Builder()
                .setTargetResolution(Size(1280, 720))
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()

            imageAnalyzer.setAnalyzer(Executors.newSingleThreadExecutor()) { imageProxy ->
                val mediaImage = imageProxy.image
                if (mediaImage != null) {
                    val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

                    barcodeScanner.process(image)
                        .addOnSuccessListener { barcodes ->
                            for (barcode in barcodes) {
                                barcode.rawValue?.let { value ->
                                    onScanned(value)
                                }
                            }
                        }
                        .addOnFailureListener { e ->
                            onError(e.localizedMessage ?: "Erro desconhecido")
                        }
                        .addOnCompleteListener {
                            textRecognizer.process(image)
                                .addOnSuccessListener { visionText ->
                                    for (block in visionText.textBlocks) {
                                        for (i in block.lines.indices) {
                                            val line = block.lines[i]
                                            if (line.text.contains("REF:")) {
                                                if (i + 1 < block.lines.size) {
                                                    val refNumber = block.lines[i + 1].text.trim()
                                                    onScanned(refNumber)
                                                }
                                            }
                                        }
                                    }
                                }
                                .addOnFailureListener { e ->
                                    onError(e.localizedMessage ?: "Erro desconhecido")
                                }
                                .addOnCompleteListener {
                                    imageProxy.close()
                                }
                        }
                }
            }

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                context as LifecycleOwner,
                cameraSelector,
                preview,
                imageAnalyzer
            )
        } catch (exc: Exception) {
            onError(exc.localizedMessage ?: "Erro ao iniciar a câmera")
            Log.e("BarcodeScannerView", "Erro ao iniciar a câmera", exc)
        }
    }
}