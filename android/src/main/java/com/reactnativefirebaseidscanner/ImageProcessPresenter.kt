package com.reactnativefirebaseidscanner

import android.graphics.Bitmap
import android.graphics.Rect
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.text.FirebaseVisionText

class ImageProcessPresenterPresenter(private val view: ScannerView) {

  fun runTextRecognition(selectedImage: Bitmap) {
    val image = FirebaseVisionImage.fromBitmap(selectedImage)
    val detector = FirebaseVision.getInstance().onDeviceTextRecognizer
    detector.processImage(image)
      .addOnSuccessListener { texts ->
        processTextRecognitionResult(texts)
      }
      .addOnFailureListener { e ->
        // Task failed with an exception
        e.printStackTrace()
      }
  }

  private fun processTextRecognitionResult(texts: FirebaseVisionText) {
    if (texts.textBlocks.size == 0) {
      view.editText.setText("No Text Found!!!!")
      return
    }

    for (block in texts.textBlocks) {
      for (line in block.lines) {
        for (element in line.elements) {
          view.showHandle(element.text, element.boundingBox)
        }
      }
    }
  }


  private fun looksLikeHandle(text: String) =
    text.matches(Regex("@(\\w+)"))

}
