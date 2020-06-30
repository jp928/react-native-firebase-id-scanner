package com.reactnativefirebaseidscanner

import android.graphics.Bitmap
import android.util.Log
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.WritableMap
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.text.FirebaseVisionText


class ImageProcessPresenterPresenter() {

  var view: ScannerView? = null

//  var activity: AppCompatActivity? null

  fun runTextRecognition(selectedImage: Bitmap, callback: ((map: WritableMap) -> Unit)? = null) {
    val image = FirebaseVisionImage.fromBitmap(selectedImage)
    val detector = FirebaseVision.getInstance().onDeviceTextRecognizer
    detector.processImage(image)
      .addOnSuccessListener { texts ->
        val map = this.processTextRecognitionResult(texts)
        if (callback != null) {
          callback(map)
        }
      }
      .addOnFailureListener { e ->
        // Task failed with an exception
        e.printStackTrace()
      }
  }

  private fun processTextRecognitionResult(texts: FirebaseVisionText): WritableMap {
//    if (texts.textBlocks.size == 0) {
//      view.editText.setText("No Text Found!!!!")
//      return
//    }
    var text = ""
    for (block in texts.textBlocks) {
      for (line in block.lines) {
        for (element in line.elements) {
          view?.showBox(element.boundingBox)

          text += element.text
        }
      }
    }

    val driverLicenseNoMatches = Regex("(?i)(LicenceNo(\\.)?)?(\\d{3}([a-z]|\\s+)?\\d{3}([a-z]|\\s+)?\\d{2,3})")
        .findAll(text)

    var licenseNo: String = ""
    driverLicenseNoMatches.forEach { matchResult ->
      licenseNo = matchResult.value
      if (matchResult.groupValues[1].isNotEmpty()) {
        licenseNo = matchResult.groupValues.takeLast(1).toString()
      }
    }

    view?.editText?.append(licenseNo)

    val dateOfBirthMathes = Regex("(?i)(DateofBirth|DOB)?(\\d{2}(?i)(?:Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Sept|Oct|Nov|Dec)\\d{2,4})")
      .findAll(text)

    var dob = ""
    dateOfBirthMathes.forEach { matchResult ->
      dob = matchResult.value
      if (matchResult.groupValues[1].isNotEmpty()) {
        dob = matchResult.groupValues.takeLast(1).toString()
      }
    }

    val expiryMatches = Regex("(?i)(ExpiryDate|Expiry)?((\\d{2}(?i)(?:Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Sept|Oct|Nov|Dec)(?:20[2-9][0-9]))|((?:0[1-9]|1[0-9]|2[0-8])[\\.](?:0[1-9]|1[012])[\\.](?:2[0-9]|3[0-9])))")
      .findAll(text)

    var expiry = ""
    expiryMatches.forEach { matchResult ->
      dob = matchResult.value
      if (matchResult.groupValues[1].isNotEmpty()) {
        expiry = matchResult.groupValues.takeLast(1).toString()
      }
    }

    Log.v("test", licenseNo)
    Log.v("test", dob)
    Log.v("test", expiry)
    Log.v("test", text)

    val map: WritableMap = Arguments.createMap()
    map.putString("licenseNo", licenseNo)
    map.putString("dob", dob)
    map.putString("expiry", expiry)

    return map
  }


}
