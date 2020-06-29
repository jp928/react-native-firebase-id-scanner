package com.reactnativefirebaseidscanner

import android.content.Intent
import android.provider.MediaStore
import com.facebook.react.bridge.Callback
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod

class FirebaseIdScannerModule constructor(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {
  override fun getName(): String {
    return "FirebaseIdScanner"
  }

  private var callback: Callback? = null
  private var errorCallback: Callback? = null

  @ReactMethod
  fun openCamera() {
//    this.callback = callback
//    this.errorCallback = errorCallback

    val intent = Intent(currentActivity, CameraActivity::class.java)
    currentActivity!!.startActivity(intent)
  }
}
