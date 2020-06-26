package com.reactnativefirebaseidscanner

import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.net.Uri
import android.util.AttributeSet
import android.view.View
import java.util.*


class Overlay(context: Context, attrs: AttributeSet) : View(context, attrs) {

  private val lock = Any()
  private val graphics = HashSet<Graphic>()
  private val handles = mutableListOf<Overlay.Handle>()

  init {
    setOnTouchListener { _, event ->
      openTwitterIfProfileClicked(event.x, event.y)
    }
  }

  private fun openTwitterIfProfileClicked(x: Float, y: Float): Boolean {
    return handles.find { it.boundingBox?.contains(x.toInt(), y.toInt()) ?: false }?.let {
      openTwitterProfile(it.text)
      true
    } ?: run {
      false
    }
  }

  private fun openTwitterProfile(handle: String) {
    val url = "https://twitter.com/" + handle.trim().removePrefix("@")
    val browserIntent = Intent(Intent.ACTION_VIEW,
      Uri.parse(url))
    context.startActivity(browserIntent)
  }

  /**
   * Base class for a custom graphics object to be rendered within the graphic overlay. Subclass
   * this and implement the [Graphic.draw] method to define the graphics element. Add
   * instances to the overlay using [Overlay.add].
   */
  abstract class Graphic(private val overlay: Overlay) {

    /**
     * Draw the graphic on the supplied canvas. Drawing should use the following methods to convert
     * to view coordinates for the graphics that are drawn:
     *
     *
     *
     *
     * @param canvas drawing canvas
     */
    abstract fun draw(canvas: Canvas)

    fun postInvalidate() {
      overlay.postInvalidate()
    }
  }

  /**
   * Removes all graphics from the overlay.
   */
  fun clear() {
    synchronized(lock) {
      graphics.clear()
    }
    postInvalidate()
  }

  /**
   * Adds a graphic to the overlay.
   */
  private fun add(graphic: Graphic) {
    synchronized(lock) {
      graphics.add(graphic)
    }
    postInvalidate()
  }

  class Handle(val text: String, val boundingBox: Rect?)

  fun addText(text: String, boundingBox: Rect?) {
    add(TextGraphic(this, boundingBox))
    handles.add(Handle(text, boundingBox))
  }

  fun addBox(boundingBox: Rect?) {
    add(TextGraphic(this, boundingBox, Color.RED))
  }

  /**
   * Draws the overlay with its associated graphic objects.
   */
  override fun onDraw(canvas: Canvas) {
    super.onDraw(canvas)

    synchronized(lock) {
      for (graphic in graphics) {
        graphic.draw(canvas)
      }
    }
  }
}
