package fl.mlkit.text.recognize

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.graphics.Point
import android.graphics.Rect
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizerOptions
import fl.camera.FlCameraMethodCall
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel

class FlMlKitTextRecognizeMethodCall(
    activityPlugin: ActivityPluginBinding,
    plugin: FlutterPlugin.FlutterPluginBinding
) :
    FlCameraMethodCall(activityPlugin, plugin) {

    private var scan = false

    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        when (call.method) {
            "startPreview" -> startPreview(imageAnalyzer, call, result)
            "scanImageByte" -> scanImageByte(call, result)
            "scan" -> {
                val argument = call.arguments as Boolean
                if (argument != scan) {
                    scan = argument
                }
                result.success(true)
            }
            else -> {
                super.onMethodCall(call, result)
            }
        }
    }

    private fun scanImageByte(call: MethodCall, result: MethodChannel.Result) {
        val useEvent = call.argument<Boolean>("useEvent")!!
        val byteArray = call.argument<ByteArray>("byte")!!
        var rotationDegrees = call.argument<Int>("rotationDegrees")
        val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        if (bitmap == null) {
            result.success(null)
            return
        }
        if (rotationDegrees == null) rotationDegrees = 0
        val inputImage = InputImage.fromBitmap(bitmap, rotationDegrees)
        analysis(inputImage, if (useEvent) null else result, null)
    }

    @SuppressLint("UnsafeOptInUsageError")
    private val imageAnalyzer = ImageAnalysis.Analyzer { imageProxy ->
        val mediaImage = imageProxy.image
        if (mediaImage != null && scan) {
            val inputImage =
                InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
            analysis(inputImage, null, imageProxy)
        } else {
            imageProxy.close()
        }
    }


    private fun analysis(
        inputImage: InputImage,
        result: MethodChannel.Result?,
        imageProxy: ImageProxy?
    ) {

        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
        recognizer.process(inputImage)
            .addOnSuccessListener { visionText ->
                if (result == null) {
                    flCameraEvent?.sendEvent(visionText.data)
                } else {
                    result.success(visionText.data)
                }
            }
            .addOnFailureListener { result?.success(null) }
            .addOnCompleteListener { imageProxy?.close() }

    }

    private val Text.data: Map<String, Any?>
        get() = mapOf(
            "text" to text,
            "textBlocks" to textBlocks.map { corner -> corner.data }
        )

    private val Text.TextBlock.data: Map<String, Any?>
        get() = mapOf(
            "text" to text,
            "recognizedLanguage" to recognizedLanguage,
            "boundingBox" to boundingBox?.data,
            "corners" to cornerPoints?.map { corner -> corner.data },
            "lines" to lines.map { corner -> corner.data },
        )
    private val Text.Element.data: Map<String, Any?>
        get() = mapOf(
            "text" to text,
            "recognizedLanguage" to recognizedLanguage,
            "boundingBox" to boundingBox,
            "corners" to cornerPoints?.map { corner -> corner.data },
            "recognizedLanguage" to recognizedLanguage,
        )
    private val Text.Line.data: Map<String, Any?>
        get() = mapOf(
            "text" to text,
            "recognizedLanguage" to recognizedLanguage,
            "boundingBox" to boundingBox,
            "corners" to cornerPoints?.map { corner -> corner.data },
            "elements" to elements.map { corner -> corner.data },
        )

    private val Rect.data: Map<String, Int>
        get() = mapOf("top" to top, "bottom" to bottom, "left" to left, "right" to right)

    private val Point.data: Map<String, Double>
        get() = mapOf("x" to x.toDouble(), "y" to y.toDouble())
}
