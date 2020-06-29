package com.reactnativefirebaseidscanner

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Rect
import android.provider.MediaStore
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.facebook.react.bridge.ReactContext


class ScannerView constructor(
  context: Context
) : ConstraintLayout(context) {

  var imageView: ImageView
  var editText: EditText
  var overlay: Overlay
  val TAKE_PHOTO_ACTION = 1
  val SELECT_PHOTO_ACTION = 2

  init {
    LayoutInflater.from(context)
      .inflate(R.layout.scanner, this, true)

    imageView = findViewById(R.id.imageView)
    editText = findViewById(R.id.editText)
    overlay = findViewById(R.id.overlay)

    val selectImageBtn = findViewById<Button>(R.id.select_image_btn)
    val takePhotoBtn = findViewById<Button>(R.id.take_photo_btn)

    val permissionCheck: Int = ContextCompat.checkSelfPermission(
      getContext(),
      Manifest.permission.CAMERA
    )

    if (permissionCheck !== PackageManager.PERMISSION_GRANTED) {
      getActivity()?.let {
        ActivityCompat.requestPermissions(it, arrayOf(Manifest.permission.CAMERA),
          1)
      }
    }

    // set on-click listener
    takePhotoBtn.setOnClickListener {
      getActivity()?.startActivityForResult(Intent(MediaStore.ACTION_IMAGE_CAPTURE), TAKE_PHOTO_ACTION, null)
    }

    // set on-click listener
    selectImageBtn.setOnClickListener {
      val intent = Intent()
      intent.type = "image/*"
      intent.action = Intent.ACTION_GET_CONTENT
      getActivity()?.startActivityForResult(intent, SELECT_PHOTO_ACTION, null)
    }
  }

  private fun getActivity(): Activity? {
    val context = context
    if (context is Activity) {
      return context
    }
    if (context is ReactContext) {
      val reactContext = context as ReactContext
      return reactContext.currentActivity
    }
    return null
  }

  fun setScaleFactorX(scaleFactor: Float) {
    overlay.scaleFactorX = scaleFactor
  }

  fun setScaleFactorY(scaleFactor: Float) {
    overlay.scaleFactorY = scaleFactor
  }

  fun showHandle(text: String, boundingBox: Rect?) {
    overlay.addText(text, boundingBox)
  }

  fun showBox(boundingBox: Rect?) {
    overlay.addBox(boundingBox)
  }

}
