package com.reactnativefirebaseidscanner

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
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
    return MediaStore.Images.Media.getBitmap(this.context.currentActivity?.contentResolver, filePath)
  }

  private fun resizeImage(selectedImage: Uri): Bitmap? {
    return getBitmapFromUri(selectedImage)?.let {
      val scaleFactor = Math.max(
        it.width.toFloat() / scannerView.imageView.width.toFloat(),
        it.height.toFloat() / scannerView.imageView.height.toFloat())

      Bitmap.createScaledBitmap(it,
        (it.width / scaleFactor).toInt(),
        (it.height / scaleFactor).toInt(),
        true)
    }
  }

  private val mActivityEventListener: ActivityEventListener = object : BaseActivityEventListener() {
    override fun onActivityResult(activity: Activity, requestCode: Int, resultCode: Int, intent: Intent) {

      Log.v("Test", resultCode.toString());
      when (requestCode) {
        1 -> if (resultCode == Activity.RESULT_OK) {
          Log.v("Test", "HELLO WORLD333");
          val imageBitmap = intent.extras.get("data") as Bitmap
          scannerView.imageView.setImageBitmap(imageBitmap)
          scannerView.overlay.clear()
          presenter.runTextRecognition(imageBitmap)
        }
      }
    }
  }

  init {
    context.addActivityEventListener(mActivityEventListener)
  }

  override fun getName() = "ScannerView"
  override fun createViewInstance(reactContext: ThemedReactContext): ScannerView {
    scannerView = ScannerView(reactContext)
    this.presenter = ImageProcessPresenterPresenter(scannerView)
    return scannerView
  }
}
