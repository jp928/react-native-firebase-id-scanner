package com.reactnativefirebaseidscanner

import android.graphics.*

/**
 * Graphic instance for rendering TextBlock position, size, and ID within an associated graphic
 * overlay view.
 */
class TextGraphic internal constructor(
  overlay: Overlay,
  private val boundingBox: Rect?,
  private val color: Int = Color.BLUE) :
  Overlay.Graphic(overlay) {

  private val rectPaint: Paint = Paint()

  init {
    rectPaint.color = Color.WHITE
    rectPaint.style = Paint.Style.STROKE
    rectPaint.strokeWidth = STROKE_WIDTH + 2

    // Redraw the overlay, as this graphic has been added.
    postInvalidate()
  }

  /**
   * Draws the text block annotations for position, size, and raw value on the supplied canvas.
   */
  override fun draw(canvas: Canvas) {
    // Draws the bounding box around the TextBlock.
    val rect = RectF(boundingBox)
    canvas.drawRect(rect, rectPaint)
    rectPaint.color = color
    rectPaint.style = Paint.Style.STROKE
    rectPaint.strokeWidth = STROKE_WIDTH
    canvas.drawRect(rect, rectPaint)
  }

  companion object {

    private const val STROKE_WIDTH = 4.0f
  }
}
