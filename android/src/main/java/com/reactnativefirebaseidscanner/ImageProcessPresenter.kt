package com.reactnativefirebaseidscanner

import android.graphics.Bitmap
import android.graphics.Rect
import android.util.Log
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.text.FirebaseVisionText

class ImageProcessPresenterPresenter(private val view: ScannerView) {

  fun runTextRecognition(selectedImage: Bitmap) {
    val image = FirebaseVisionImage.fromBitmap(selectedImage)
    val detector = FirebaseVision.getInstance().onDeviceTextRecognizer
    detector.processImage(image)
      .addOnSuccessListener { texts ->
        this.processTextRecognitionResult(texts)
      }
      .addOnFailureListener { e ->
        // Task failed with an exception
        e.printStackTrace()
      }
  }

  private fun processTextRecognitionResult(texts: FirebaseVisionText) {
//    if (texts.textBlocks.size == 0) {
//      view.editText.setText("No Text Found!!!!")
//      return
//    }
    var text = ""
    for (block in texts.textBlocks) {
      for (line in block.lines) {
        for (element in line.elements) {
//          Log.v("test", element.text)
//          view.showHandle(element.text, element.boundingBox)
          view.showBox(element.boundingBox)
          text += element.text
//          view.editText.append(element.text)
//          view.editText.append("\n")
        }
      }
    }

    Log.v("test", text)
    val matches = Regex("(?i)(LicenceNo)?(\\d{3}([a-z]|\\s+)?\\d{3}([a-z]|\\s+)?\\d{3})")
        .findAll(text)

    var result: String = ""
    matches.forEach { matchResult ->
      result = matchResult.value
      Log.v("test", matchResult.value)
      Log.v("test", matchResult.groupValues[1])
      Log.v("test", matchResult.groupValues[2])
      if (matchResult.groupValues[1].isNotEmpty()) {
        result = matchResult.groupValues[2]
      }
    }

    view.editText.append(result)

  }


}
