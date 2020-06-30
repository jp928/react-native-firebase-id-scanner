package com.reactnativefirebaseidscanner

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import com.facebook.react.bridge.ActivityEventListener
import com.facebook.react.bridge.BaseActivityEventListener
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.uimanager.SimpleViewManager
import com.facebook.react.uimanager.ThemedReactContext

class ScannerViewManager constructor(
  context: ReactApplicationContext
) : SimpleViewManager<ScannerView>() {
  private lateinit var presenter: ImageProcessPresenterPresenter
  private lateinit var scannerView: ScannerView
  private var context: ReactApplicationContext = context

  private fun getBitmapFromUri(filePath: Uri): Bitmap? {
    return MediaStore.Images.Media.getBitmap(context.currentActivity?.contentResolver, filePath)
  }

  private fun resizeImage(selectedImage: Bitmap): Bitmap? {
    return selectedImage.let {

      val scaleFactorX =  it.width.toFloat() / scannerView.imageView.width.toFloat()
      val scaleFactorY = it.height.toFloat() / scannerView.imageView.height.toFloat()

      scannerView.setScaleFactorX(1/scaleFactorX)
      scannerView.setScaleFactorY(1/scaleFactorY)

      Bitmap.createScaledBitmap(it,
        (it.width / scaleFactorX).toInt(),
        (it.height / scaleFactorY).toInt(),
        true)
    }
  }

  private val mActivityEventListener: ActivityEventListener = object : BaseActivityEventListener() {
    override fun onActivityResult(activity: Activity, requestCode: Int, resultCode: Int, intent: Intent) {
      when (requestCode) {
        1 -> if (resultCode == Activity.RESULT_OK) {
          (intent.data as Bitmap).also {
            processImage(it)
          }
        }

        2 -> if (resultCode == Activity.RESULT_OK) {
          val imageBitmap = getBitmapFromUri(intent.data as Uri)!!
          processImage(imageBitmap)
        }
      }
    }
  }

  private fun processImage(imageBitmap: Bitmap) {
    val resizedImage = resizeImage(imageBitmap)
    scannerView.imageView.setImageBitmap(resizedImage)
    scannerView.overlay.clear()
    scannerView.editText.setText("")
    presenter.runTextRecognition(imageBitmap)
  }

  init {
    context.addActivityEventListener(mActivityEventListener)
  }

  override fun getName() = "ScannerView"
  override fun createViewInstance(reactContext: ThemedReactContext): ScannerView {
    scannerView = ScannerView(reactContext)
    presenter = ImageProcessPresenterPresenter()
    presenter.view = scannerView
    return scannerView
  }
}
