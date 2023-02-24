package com.example.mujika

//import kotlinx.android.synthetic.main.fragment_twibbon.*
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class TwibbonFragment : Fragment() {

    private var imageCapture: ImageCapture? = null
    private lateinit var outputDirectory: File
    private var cameraExecutor: ExecutorService? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(false)
        val view = inflater.inflate(R.layout.fragment_twibbon, container, false)
        val myTextView = view.findViewById<TextView>(R.id.photoText)

        val activity = requireActivity()
        outputDirectory = getOutputDirectory()
        cameraExecutor = Executors.newSingleThreadExecutor()

        if (allPermissionGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(
                activity, Constants.REQUIRED_PERMISSIONS, Constants.REQUEST_CODE_PERMISSIONS
            )
        }

        val photoButton = view.findViewById<ImageButton>(R.id.btnTakePhoto)
        photoButton.setOnClickListener {
            takePhoto()
            myTextView.text = "Take again?"
        }

        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor?.shutdown()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == Constants.REQUEST_CODE_PERMISSIONS) {
            if (allPermissionGranted()) {
                startCamera()
            } else {
                Toast.makeText(
                    context,
                    "Permission Denied",
                    Toast.LENGTH_SHORT
                ).show()
                requireActivity().finish()
            }
        }
    }

    private fun getOutputDirectory(): File {
        val mediaDir = activity?.externalMediaDirs?.firstOrNull()?.let { mFile->
            File(mFile, resources.getString(R.string.app_name)).apply{
                mkdirs()
            }
        }

        return if(mediaDir != null && mediaDir.exists())
            mediaDir else requireActivity().filesDir
    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return
        val photofiile = File(
            outputDirectory,
            SimpleDateFormat(
                Constants.FILE_NAME_FORMAT,
                Locale.getDefault()
            )
                .format(
                    System
                        .currentTimeMillis()
                ) + ".jpg"
        )

        val outputOption = ImageCapture.OutputFileOptions.Builder(photofiile).build()

        context?.let { ContextCompat.getMainExecutor(it) }?.let {
            imageCapture.takePicture(
                outputOption, it,
                object : ImageCapture.OnImageSavedCallback {
                    override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                        val savedUri = Uri.fromFile((photofiile))
                        val msg = "Photo Saved"

                        Toast.makeText(
                            context,
                            "$msg $savedUri",
                            Toast.LENGTH_LONG
                        ).show()
                        mergeTwibbon(photofiile)
                    }

                    override fun onError(exception: ImageCaptureException) {
                        Log.e(Constants.TAG, "onError: ${exception.message}", exception)
                    }
                }
            )
        }
    }

    private fun startCamera() {
        val context = requireContext()
        val cameraProviderFuture = ProcessCameraProvider
            .getInstance(context)
        cameraProviderFuture.addListener(
            {
                val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
                val preview = Preview.Builder()
                    .build()
                    .also { mPreview ->
                        mPreview.setSurfaceProvider(
                            view?.findViewById<PreviewView>(R.id.viewFinder)?.surfaceProvider
                        )
                    }
                imageCapture = ImageCapture.Builder()
                    .build()
                val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
                try {
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        this, cameraSelector,
                        preview, imageCapture
                    )
                } catch (e: Exception) {
                    Log.d(Constants.TAG, "StartCamera Fail:", e)
                }
            }, ContextCompat.getMainExecutor(context)
        )
    }

    override fun onPause() {
        Log.d("TAG", "Camera is onpause")
        super.onPause()
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            cameraProvider.unbindAll()
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    override fun onResume() {
        super.onResume()
        startCamera()
    }




    private fun allPermissionGranted() =

        Constants.REQUIRED_PERMISSIONS.all {
            context?.let { it1 ->
                ContextCompat.checkSelfPermission(
                    it1, it
                )
            } == PackageManager.PERMISSION_GRANTED
        }

    private fun mergeTwibbon(file: File) {
        val bitmap1 = BitmapFactory.decodeFile(file.absolutePath, BitmapFactory.Options())
        val exif = ExifInterface(file.absoluteFile.toString())
        val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
        val matrix = Matrix()

        when(orientation){
            ExifInterface.ORIENTATION_ROTATE_90-> matrix.postRotate(90f)
            ExifInterface.ORIENTATION_ROTATE_180-> matrix.postRotate(180f)
            ExifInterface.ORIENTATION_ROTATE_270-> matrix.postRotate(270f)
        }
        val rotatedbitmap1 = Bitmap.createBitmap(bitmap1, 0, 0, bitmap1.width, bitmap1.height, matrix, true)
        bitmap1.recycle()
        val bitmap2 = BitmapFactory.decodeResource(resources, R.drawable.pngtreeyellow_and_blue_twibbon_frame_7362651)

        val scalebitmap2 = Bitmap.createScaledBitmap(bitmap2, rotatedbitmap1.width, rotatedbitmap1.height, true)
        val mergedBitmap = Bitmap.createBitmap(rotatedbitmap1.width, rotatedbitmap1.height, rotatedbitmap1.config)
        val canvas = Canvas(mergedBitmap)
        canvas.drawBitmap(rotatedbitmap1, 0f, 0f, null)
        canvas.drawBitmap(scalebitmap2 ,0f, 0f , null)

        val photofile = File(
            outputDirectory,
            "result" + SimpleDateFormat(
                Constants.FILE_NAME_FORMAT,
                Locale.getDefault()
            )
                .format(
                    System
                        .currentTimeMillis()
                ) + ".jpg"
        )

        val stream = FileOutputStream(photofile)
        mergedBitmap.compress(Bitmap.CompressFormat.JPEG,100,stream)
        stream.close()
    }
}