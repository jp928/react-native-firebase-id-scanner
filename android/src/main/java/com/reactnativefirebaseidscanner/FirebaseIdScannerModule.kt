package com.reactnativefirebaseidscanner

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import com.facebook.react.bridge.*
import com.facebook.react.modules.core.DeviceEventManagerModule

class FirebaseIdScannerModule constructor(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {
  companion object {
    // var eventEmitter: RCTDeviceEventEmitter? = null

    private var context: ReactApplicationContext? = null

    fun emitDeviceEvent(eventName: String, eventData: WritableMap?) {
      Log.v("test", eventName)
      // A method for emitting from the native side to JS
      // https://facebook.github.io/react-native/docs/native-modules-android.html#sending-events-to-javascript
      context?.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)?.emit(eventName, eventData)
    }
  }

  init {
    context = reactContext
  }

  override fun initialize() {
    super.initialize()
    // eventEmitter = reactApplicationContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
  }

  override fun getName(): String {
    return "FirebaseIdScanner"
  }

  @ReactMethod
  fun openCamera() {
    val intent = Intent(currentActivity, CameraActivity::class.java)
    currentActivity!!.startActivity(intent)
  }
}
