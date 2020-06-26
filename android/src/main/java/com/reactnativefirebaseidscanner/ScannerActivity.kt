package com.reactnativefirebaseidscanner

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ScannerActivity : AppCompatActivity() {
  lateinit var imageView: ImageView

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
  }

//  override fun onActivityResult(requestCode: Int, resultCode: Int, imageReturnedIntent: Intent?) {
//    super.onActivityResult(requestCode, resultCode, imageReturnedIntent)
//    when (requestCode) {
//      1 -> if (resultCode == Activity.RESULT_OK) {
//        imageReturnedIntent?.data?.let {
//          val selectedImageBitmap = resizeImage(it)
//          imageView.setImageBitmap(selectedImageBitmap)
//
//          Log.v("Testing", "We are here")
////          Toast.makeText(this.context, "Select an Image First", Toast.LENGTH_LONG).show()
////          setUpCloudSearch(selectedImageBitmap)
////          overlay.clear()
////          presenter.runTextRecognition(selectedImageBitmap!!)
//        }
//      }
//    }
//  }

  private fun resizeImage(selectedImage: Uri): Bitmap? {
    return getBitmapFromUri(selectedImage)?.let {
      val scaleFactor = Math.max(
        it.width.toFloat() / imageView.width.toFloat(),
        it.height.toFloat() / imageView.height.toFloat())

      Bitmap.createScaledBitmap(it,
        (it.width / scaleFactor).toInt(),
        (it.height / scaleFactor).toInt(),
        true)
    }
  }

  private fun getBitmapFromUri(filePath: Uri): Bitmap? {
    return MediaStore.Images.Media.getBitmap(this.contentResolver, filePath)
  }

}
