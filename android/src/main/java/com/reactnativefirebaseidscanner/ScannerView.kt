package com.reactnativefirebaseidscanner

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.facebook.react.bridge.ActivityEventListener
import com.facebook.react.bridge.BaseActivityEventListener
import com.facebook.react.bridge.ReactContext


class ScannerView constructor(
  context: Context
) : LinearLayout(context) {

  lateinit var imageView: ImageView
  lateinit var editText: EditText


  init {
    LayoutInflater.from(context)
      .inflate(R.layout.scanner, this, true)

    orientation = VERTICAL

    imageView = findViewById(R.id.imageView)
    editText = findViewById(R.id.editText)

    val selectImageBtn = findViewById<Button>(R.id.select_image_btn)

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
    selectImageBtn.setOnClickListener {
      getActivity()?.startActivityForResult(Intent(MediaStore.ACTION_IMAGE_CAPTURE), 1, null)
    }
  }



//  fun startRecognizing(v: View) {
//    if (imageView.drawable != null) {
//      editText.setText("")
//      v.isEnabled = false
//      val bitmap = (imageView.drawable as BitmapDrawable).bitmap
//      val image = FirebaseVisionImage.fromBitmap(bitmap)
//      val detector = FirebaseVision.getInstance().onDeviceTextRecognizer
//
//      detector.processImage(image)
//        .addOnSuccessListener { firebaseVisionText ->
//          v.isEnabled = true
//          processResultText(firebaseVisionText)
//        }
//        .addOnFailureListener {
//          v.isEnabled = true
//          editText.setText("Failed")
//        }
//    } else {
//      Toast.makeText(this.context, "Select an Image First", Toast.LENGTH_LONG).show()
//    }

//  }

//  private fun processResultText(resultText: FirebaseVisionText) {
//    if (resultText.textBlocks.size == 0) {
//      editText.setText("No Text Found")
//      return
//    }
//    for (block in resultText.textBlocks) {
//      val blockText = block.text
//      editText.append(blockText + "\n")
//    }
//  }

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

}
