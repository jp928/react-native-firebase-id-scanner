package com.reactnativefirebaseidscanner

import android.view.View
import android.view.ViewGroup
import com.facebook.react.uimanager.SimpleViewManager
import com.facebook.react.uimanager.ViewManager
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.annotations.ReactProp

class ScannerViewManager: SimpleViewManager<ScannerView>() {
  override fun getName() = "ScannerView"
  override fun createViewInstance(reactContext: ThemedReactContext): ScannerView {
    return ScannerView(reactContext)
  }


//  @ReactProp(name = "videoId")
//  fun setVideoId(view: YouTubePlayerView, newId: String?) {
//    if (newId == null || newId == videoId) return
//    videoId = newId
//    ytPlayerView?.loadVideo(newId, 0f)
//  }
}
