package id.itborneo.facecare.analize

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import id.itborneo.facecare.R
import id.itborneo.facecare.analyzing.AnalyzingActivity
import id.itborneo.facecare.core.local.AppDatabase
import id.itborneo.facecare.core.model.ResultModel
import id.itborneo.facecare.databinding.FragmentAnalizeBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class AnalizeFragment : Fragment() {

    private var imageCapture: ImageCapture? = null

    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var binding: FragmentAnalizeBinding
    private var lensFacing: Int = CameraSelector.LENS_FACING_FRONT

    private val TAG = "AnalizeFragment"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAnalizeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        requestPermission.launch(Manifest.permission.ACCESS_COARSE_LOCATION)

        initButtonInfo()
        initSelectButton()
        initAllNeedCamera()
        privacyInfo(requireContext(), binding.root)

    }

    private fun initButtonInfo() {
        binding.btnInfo.setOnClickListener {

        }
    }

    private fun initSelectButton() {

        binding.btnSelectImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(intent, 100)
        }
    }


//    private val requestPermission =
//        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
//            if (isGranted) {
//                Log.i("DEBUG", "permission granted")
//            } else {
//                Log.i("DEBUG", "permission denied")
//            }
//
//
//        }

    private fun initAllNeedCamera() {
        Log.d(TAG, "initAllNeedCamera")
        // Request camera permissions
        if (allPermissionsGranted()) {
            Log.d(TAG, "initAllNeedCamera  allPermissionsGranted")

            startCamera()
        } else {


            Log.d(TAG, "initAllNeedCamera  request permission")


            requestPermissions(REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }


        // Set up the listener for take photo button
        binding.btnAnalize.setOnClickListener { takePhoto() }
        outputDirectory = getOutputDirectory()
        cameraExecutor = Executors.newSingleThreadExecutor()
    }


    private fun takePhoto() {
        // Get a stable reference of the modifiable image capture use case
        val imageCapture = imageCapture ?: return
        imageCapture.targetRotation = Surface.ROTATION_0

        // Create time-stamped output file to hold the image
        val photoFile = File(
            outputDirectory,
            SimpleDateFormat(
                FILENAME_FORMAT, Locale.US
            ).format(System.currentTimeMillis()) + ".jpg"
        )


        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(requireContext()),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {

                    val savedUri = Uri.fromFile(photoFile)
                    actionToAnayzing(savedUri)

                    val msg = "Opening Analyzing"
                    Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
                    Log.d(TAG, msg)

                }
            })
    }

    private fun actionToAnayzing(savedUri: Uri) {

        val result = ResultModel(
            image_url = savedUri.toString(),
            date = "Sekarang"
        )
        addToTableDb(result) {
            val isFront = lensFacing == CameraSelector.LENS_FACING_FRONT
            //move to analyzing
            AnalyzingActivity.getInstance(requireContext(), savedUri, isFront)

        }

    }

    private fun startCamera() {

        bindCameraUseCases()

        val switchButton = binding.btnSwitchCamera
        switchButton.setOnClickListener {
            // Disable the button until the camera is set up
//            it.isEnabled = false

            // Listener for button used to switch cameras. Only called if the button is enabled
            lensFacing = if (CameraSelector.LENS_FACING_FRONT == lensFacing) {
                CameraSelector.LENS_FACING_BACK
            } else {
                CameraSelector.LENS_FACING_FRONT
            }
            // Re-bind use cases to update selected camera
            bindCameraUseCases()

        }


    }


    private fun bindCameraUseCases() {

        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener({
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
                }

            imageCapture = ImageCapture.Builder()
                .build()

//            val orientationEventListener = object : OrientationEventListener(this as Context) {
//                override fun onOrientationChanged(orientation: Int) {
//                    // Monitors orientation values to determine the target rotation value
//                    val rotation: Int = when (orientation) {
//                        in 45..134 -> Surface.ROTATION_270
//                        in 135..224 -> Surface.ROTATION_180
//                        in 225..314 -> Surface.ROTATION_90
//                        else -> Surface.ROTATION_0
//                    }
//
//                    imageCapture!!.targetRotation = rotation
//                }
//            }
//            orientationEventListener.enable()


            // Select back camera as a default
//            val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
            // CameraSelector
            val cameraSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()

            try {

                // Unbind use cases before rebinding
                cameraProvider?.unbindAll()

                // Bind use cases to camera
                cameraProvider?.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture
                )

            } catch (exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(requireContext()))

    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            requireContext(), it
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun getOutputDirectory(): File {
        val mediaDir = requireContext().externalMediaDirs.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else requireContext().filesDir
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    companion object {
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults:
        IntArray
    ) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT
                ).show()

                requireActivity().onBackPressed()
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val uri = data?.data

        if (uri != null) {
            actionToAnayzing(uri)
        }

    }

    private fun privacyInfo(context: Context, rootView: ViewGroup) {
        val dialog = BottomSheetDialog(context, R.style.SheetDialog)
//        dialog.behavior.isHideable = false
        dialog.behavior.state = BottomSheetBehavior.STATE_COLLAPSED

        val view =
            LayoutInflater.from(context).inflate(R.layout.bottom_sheet_detail, rootView, false)
        dialog.setContentView(view)

        view?.isFocusableInTouchMode = true
        view?.requestFocus()
        view?.setOnKeyListener { _, keyCode, _ ->
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                // Do what you want to do on back press
                dialog.dismiss()
//                back()
                true
            } else
                false
        }


        val root = view?.findViewById<Button>(R.id.btnClose)
//
//        updateFavorite(city.isfavorite, ivFavorite, true)
//
        root?.setOnClickListener {
            dialog.dismiss()
        }
//}
//            city.isfavorite = !city.isfavorite
//            Log.d(TAG, "ivFavorite ${city.isfavorite}")
//
//            KsPref.prefs.push(city.id, city.isfavorite)
//            KsPref.prefs.save()
//            updateFavorite(city.isfavorite, ivFavorite)
//
//        }


        binding.btnInfo.setOnClickListener {
            dialog.show()

        }
        dialog.show()
    }

    private fun back() {
        requireActivity().onBackPressed()
    }


    private fun addToTableDb(result: ResultModel, callback: () -> Unit) {

        val dao = AppDatabase.getInstance(requireContext()).resultDao()

        GlobalScope.launch(Dispatchers.IO) {
            dao.add(result)

            withContext(Dispatchers.Main) {
                callback()
            }

        }


    }
}