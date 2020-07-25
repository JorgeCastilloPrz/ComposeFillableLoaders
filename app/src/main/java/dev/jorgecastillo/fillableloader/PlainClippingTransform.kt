package dev.jorgecastillo.fillableloader

import androidx.ui.geometry.Size
import androidx.ui.graphics.ClipOp
import androidx.ui.graphics.drawscope.DrawScope
import androidx.ui.graphics.drawscope.clipRect

fun DrawScope.plainClip(
  currentFillPhase: Float,
  originalVectorSize: Size,
  block: DrawScope.() -> Unit
) {
  val left = 0f
  val top = 0f
  val right = originalVectorSize.width
  val bottom = originalVectorSize.height

  this.clipRect(
    left = left,
    top = (bottom - top) * (1f - currentFillPhase),
    right = right,
    bottom = bottom,
    clipOp = ClipOp.intersect,
    block = block
  )
}
