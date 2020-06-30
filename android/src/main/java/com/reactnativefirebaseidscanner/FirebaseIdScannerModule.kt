package com.reactnativefirebaseidscanner

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import com.facebook.react.bridge.*

class FirebaseIdScannerModule constructor(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {
  override fun getName(): String {
    return "FirebaseIdScanner"
  }

  private val mActivityEventListener: ActivityEventListener = object : BaseActivityEventListener() {
    override fun onActivityResult(activity: Activity, requestCode: Int, resultCode: Int, intent: Intent) {
      when (requestCode) {
        101 -> if (resultCode == Activity.RESULT_OK) {

        }
      }
    }
  }

  init {
      reactContext.addActivityEventListener(mActivityEventListener)
  }

  private var callback: Callback? = null
  private var errorCallback: Callback? = null

  @ReactMethod
  fun openCamera() {
    val intent = Intent(currentActivity, CameraActivity::class.java)
    currentActivity!!.startActivity(intent)
  }
}
