package com.example.mujika

import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import kotlinx.android.synthetic.main.fragment_twibbon.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class TwibbonFragment : Fragment() {

    private var imageCapture: ImageCapture? = null
    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_twibbon, container, false)
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

        val photoButton = view.findViewById<MaterialButton>(R.id.btnTakePhoto)
        photoButton.setOnClickListener {
            takePhoto()
        }

        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
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
                            viewFinder.surfaceProvider
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

    private fun allPermissionGranted() =

        Constants.REQUIRED_PERMISSIONS.all {
            context?.let { it1 ->
                ContextCompat.checkSelfPermission(
                    it1, it
                )
            } == PackageManager.PERMISSION_GRANTED
        }
}