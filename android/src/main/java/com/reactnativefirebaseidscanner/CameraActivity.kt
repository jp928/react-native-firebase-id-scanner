package com.reactnativefirebaseidscanner

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.facebook.react.ReactActivity
import com.facebook.react.bridge.WritableMap
import com.facebook.react.modules.core.DeviceEventManagerModule.RCTDeviceEventEmitter
import io.fotoapparat.Fotoapparat
import io.fotoapparat.configuration.CameraConfiguration
import io.fotoapparat.log.logcat
import io.fotoapparat.log.loggers
import io.fotoapparat.parameter.ScaleType
import io.fotoapparat.selector.back
import io.fotoapparat.selector.front
import io.fotoapparat.selector.off
import io.fotoapparat.selector.torch
import io.fotoapparat.view.CameraView
import io.fotoapparat.view.FocusView


class CameraActivity : ReactActivity() {
  var fotoapparat: Fotoapparat? = null
  var fotoapparatState: FotoapparatState? = null
  var cameraStatus: CameraState? = null
  var flashState: FlashState? = null
//  var mainActivity: AppCompatActivity? = null

  lateinit var presenter: ImageProcessPresenterPresenter

  val permissions = arrayOf(android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.camera)

    val fabCapture = findViewById<View>(R.id.fab_capture)

    createFotoapparat()

    cameraStatus = CameraState.BACK
    flashState = FlashState.OFF
    fotoapparatState = FotoapparatState.OFF

    presenter = ImageProcessPresenterPresenter()

//    presenter.activity = this

    fabCapture.setOnClickListener {
      takePhoto()
    }

//    fab_camera.setOnClickListener {
//      takePhoto()
//    }
//
//    fab_switch_camera.setOnClickListener {
//      switchCamera()
//    }
//
//    fab_flash.setOnClickListener {
//      changeFlashState()
//    }
  }

  private fun sendResult(map: WritableMap) {
    Log.v("test", "Here")
    reactInstanceManager.currentReactContext
      ?.getJSModule(RCTDeviceEventEmitter::class.java)
            ?.emit("onSuccess", map)

    this.finish()
  }

  private fun createFotoapparat() {
    val cameraView = findViewById<CameraView>(R.id.camera_view)
    val focusView = findViewById<FocusView>(R.id.focus_view)

    fotoapparat = Fotoapparat(
      context = this,
      view = cameraView,
      focusView = focusView,
      scaleType = ScaleType.CenterCrop,
      lensPosition = back(),
      logger = loggers(
        logcat()
      ),
      cameraErrorCallback = { error ->
        println("Recorder errors: $error")
      }
    )
  }

  private fun changeFlashState() {
    fotoapparat?.updateConfiguration(
      CameraConfiguration(
        flashMode = if (flashState == FlashState.TORCH) off() else torch()
      )
    )

    if (flashState == FlashState.TORCH) flashState = FlashState.OFF
    else flashState = FlashState.TORCH
  }

  private fun switchCamera() {
    fotoapparat?.switchTo(
      lensPosition = if (cameraStatus == CameraState.BACK) front() else back(),
      cameraConfiguration = CameraConfiguration()
    )

    if (cameraStatus == CameraState.BACK) cameraStatus = CameraState.FRONT
    else cameraStatus = CameraState.BACK
  }

  /**
   * TODO start activity with result and process image
   */
  private fun takePhoto() {
    fotoapparat
      ?.takePicture()
      ?.toBitmap()
      ?.whenAvailable { bitmapPhoto ->
        presenter.runTextRecognition(bitmapPhoto!!.bitmap, this::sendResult)
      }
  }

  override fun onStart() {
    super.onStart()
    if (hasNoPermissions()) {
      requestPermission()
    } else {
      fotoapparat?.start()
      fotoapparatState = FotoapparatState.ON
    }
  }

  /**
   * TODO: Ask permission before start this activity
   */
  private fun hasNoPermissions(): Boolean {
    return ContextCompat.checkSelfPermission(this,
      Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,
      Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,
      Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
  }

  fun requestPermission() {
    ActivityCompat.requestPermissions(this, permissions, 0)
  }


  override fun onStop() {
    super.onStop()
    fotoapparat?.stop()
    FotoapparatState.OFF
  }

  override fun onResume() {
    super.onResume()
    if (!hasNoPermissions() && fotoapparatState == FotoapparatState.OFF) {
      val intent = Intent(baseContext, CameraActivity::class.java)
      startActivity(intent)
      finish()
    }
  }

}

enum class CameraState {
  FRONT, BACK
}

enum class FlashState {
  TORCH, OFF
}

enum class FotoapparatState {
  ON, OFF
}
