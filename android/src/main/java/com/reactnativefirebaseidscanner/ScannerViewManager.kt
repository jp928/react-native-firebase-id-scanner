package com.reactnativefirebaseidscanner

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.View
import android.view.ViewGroup
import com.facebook.react.bridge.ActivityEventListener
import com.facebook.react.uimanager.SimpleViewManager
import com.facebook.react.uimanager.ViewManager
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.annotations.ReactProp

class ScannerViewManager: SimpleViewManager<ScannerView>(), ActivityEventListener {
  override fun getName() = "ScannerView"
  override fun createViewInstance(reactContext: ThemedReactContext): ScannerView {
    reactContext.addActivityEventListener(this)
    return ScannerView(reactContext)
  }

  override fun onNewIntent(intent: Intent?) = Unit

  override fun onActivityResult(activity: Activity?, requestCode: Int,
                                resultCode: Int, data: Intent?) {
//    if (requestCode == 1) {
    Log.v("Test", "HELLO WORLD2");
//    }
  }

//  @ReactProp(name = "videoId")
//  fun setVideoId(view: YouTubePlayerView, newId: String?) {
//    if (newId == null || newId == videoId) return
//    videoId = newId
//    ytPlayerView?.loadVideo(newId, 0f)
//  }
}
