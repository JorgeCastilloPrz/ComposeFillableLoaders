package dev.jorgecastillo.watercat

import androidx.ui.graphics.ClipOp
import androidx.ui.graphics.drawscope.DrawScope
import androidx.ui.graphics.drawscope.clipRect

fun DrawScope.plainClip(currentFillPhase: Float, block: DrawScope.() -> Unit) {
  val halfWidth = (size.width / 2f)
  val halfHeight = (size.height / 2f)

  val left = center.dx - halfWidth
  val top = center.dy - halfHeight
  val right = center.dx + halfWidth
  val bottom = center.dy + halfHeight

  this.clipRect(
    left = left,
    top = (bottom - top) * (1f - currentFillPhase),
    right = right,
    bottom = bottom,
    clipOp = ClipOp.intersect,
    block = block
  )
}
