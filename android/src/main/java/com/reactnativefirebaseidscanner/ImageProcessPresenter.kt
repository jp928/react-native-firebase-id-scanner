package com.reactnativefirebaseidscanner

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.WritableMap
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.text.FirebaseVisionText
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.format.DateTimeParseException
import java.util.*


class ImageProcessPresenterPresenter() {

  var view: ScannerView? = null

//  var activity: AppCompatActivity? null

  @RequiresApi(Build.VERSION_CODES.O)
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

  @RequiresApi(Build.VERSION_CODES.O)
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

          text += element.text + "|"
        }
      }
    }

    val driverLicenseNoMatches = Regex("(?i)(LicenceNo(\\.)?)?(\\d{8,9})")
      .findAll(text)
    var licenseNo: String = ""
    driverLicenseNoMatches.forEach { matchResult ->
      licenseNo = matchResult.value
      if (matchResult.groupValues[1].isNotEmpty()) {
        licenseNo = matchResult.groupValues.takeLast(1).toString()
      }
    }


    val driverLicenseNoMatchesSecondary = Regex("(?i)(LicenceNo(\\.)?)?(\\d{3}([a-z]|\\s+|\\|)?\\d{3}([a-z]|\\s+|\\|)?\\d{2,3})")
      .findAll(text)

    if (licenseNo.isEmpty()) {
      driverLicenseNoMatchesSecondary.forEach { matchResult ->
        licenseNo = matchResult.value
        if (matchResult.groupValues[1].isNotEmpty()) {
          licenseNo = matchResult.groupValues.takeLast(1).toString()
        }
      }
    }

    view?.editText?.append(licenseNo)

    val dateOfBirthMatches = Regex("(?i)(Birth|DOB)?(\\|)(\\d{2}(?i)\\|(?:Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Sept|Oct|Nov|Dec)\\|\\d{2,4})")
      .findAll(text)

    var dob = ""
    dateOfBirthMatches.forEach { matchResult ->
      var dobLike = matchResult.value.removePrefix("|").replace("|", "-")
      if (matchResult.groupValues[1].isNotEmpty()) {
        dobLike = matchResult.groupValues.takeLast(1)
          .toString()
          .removePrefix("|")
          .replace("|", "-")
          .replace("[", "")
          .replace("]", "")
      }

      val parseDate = parseDate(dobLike)

      if (parseDate?.isBefore(LocalDate.now()) == true) {
        dob = dobLike
      }
    }

    val expiryMatches = Regex("(?i)(ExpiryDate|Expiry)?((\\d{2}(?i)\\|(?:Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Sept|Oct|Nov|Dec)\\|(?:20[2-9][0-9]))|((?:0[1-9]|1[0-9]|2[0-8])[\\.](?:0[1-9]|1[012])[\\.](?:2[0-9]|3[0-9])))")
      .findAll(text)

    var expiry = ""
    expiryMatches.forEach { matchResult ->
      var expiryLike = matchResult.value.removePrefix("|").replace("|", "-")
      if (matchResult.groupValues[1].isNotEmpty()) {
        expiryLike = matchResult.groupValues.takeLast(1)
          .toString()
          .removePrefix("|")
          .replace("|", "-")
          .replace("[", "")
          .replace("]", "")
      }

      val parseDate = parseDate(expiryLike)
      if (parseDate?.isAfter(LocalDate.now()) == true) {
        expiry = expiryLike
      }
    }

    val map: WritableMap = Arguments.createMap()
    map.putString("licenseNo", licenseNo.replace("|", ""))
    map.putString("dob", dob)
    map.putString("expiry", expiry)

    return map
  }

  @RequiresApi(Build.VERSION_CODES.O)
  private val FORMATTERS: List<DateTimeFormatter> = Arrays.asList(
    DateTimeFormatter.ofPattern("dd-MMM-yyyy"),
    DateTimeFormatter.ofPattern("dd-MM-yy"),
    DateTimeFormatter.ofPattern("dd-MM-yyyy")
  )

  @RequiresApi(Build.VERSION_CODES.O)
  fun parseDate(inputString: String): LocalDate? {
    for (formatter in FORMATTERS) {
      try {
        val format = DateTimeFormatterBuilder()
          .parseCaseInsensitive()
          .parseLenient()
          .append(formatter)
          .toFormatter(Locale.ENGLISH)

        return LocalDate.parse(inputString, format)
      } catch (@SuppressLint("NewApi") e: DateTimeParseException) {
        Log.v("TextRecognition", "Cannot parse " + inputString + ":" + e.message)
        // Go on to the next format
      }
    }
    return null
  }
}
