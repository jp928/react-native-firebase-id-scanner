package com.reactnativefirebaseidscanner

import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule

class FirebaseIdScannerModule constructor(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {
  override fun getName(): String {
    return "FirebaseIdScanner"
  }
}
