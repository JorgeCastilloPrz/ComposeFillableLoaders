package dev.jorgecastillo.watercat

import android.util.Log
import androidx.animation.LinearOutSlowInEasing
import androidx.compose.Composable
import androidx.compose.state
import androidx.ui.core.Modifier
import androidx.ui.foundation.Canvas
import androidx.ui.graphics.Color
import androidx.ui.graphics.drawscope.DrawScope
import androidx.ui.graphics.drawscope.Fill
import androidx.ui.graphics.drawscope.Stroke
import androidx.ui.graphics.vector.PathParser
import androidx.ui.layout.fillMaxSize
import kotlin.math.max
import kotlin.math.min

private const val fillDuration = 10000
private const val strokeDrawingDuration = 1000

val animInterpolator = LinearOutSlowInEasing

@Composable
fun WaterCat() {
  val initialTime = System.currentTimeMillis()
  val state = state { WaterCatState.STROKE_STARTED }

  fun getFillPercent(elapsedTime: Long): Float {
    return max(0f, min(1f, (elapsedTime - strokeDrawingDuration) / fillDuration.toFloat()))
  }

  fun keepDrawing(elapsedTime: Long): Boolean {
    Log.d("CAT", "${elapsedTime < strokeDrawingDuration + fillDuration}")
    return elapsedTime < strokeDrawingDuration + fillDuration
  }

  fun DrawScope.drawStroke(elapsedTime: Long) {
    val strokePercent = max(0f, min(1f, elapsedTime * 1f / strokeDrawingDuration))

    val amountOfNodesToDraw = (animInterpolator(
      strokePercent
    ) * catPathNodes().size).toInt()

    val path = PathParser().addPathNodes(
      catPathNodes().take(amountOfNodesToDraw)).toPath()
    this.drawPath(path, Color.Blue, style = Stroke(4f))
  }

  Canvas(modifier = Modifier.fillMaxSize()) {
    val elapsedTime = System.currentTimeMillis() - initialTime
    drawStroke(elapsedTime)

    // Is stoke completely drawn.
    if (elapsedTime > strokeDrawingDuration) {
      if (state.value < WaterCatState.FILL_STARTED) {
        state.value =
          WaterCatState.FILL_STARTED
        Log.d("CAT", "Elapsed: $elapsedTime, State -> FILL_STARTED")
      }

      val fillPercent = getFillPercent(elapsedTime)
      plainClip(fillPercent) {
        drawPath(catPath(), Color.Blue, style = Fill)
      }
    }

    if (!keepDrawing(elapsedTime)) {
      state.value =
        WaterCatState.FINISHED
      Log.d("CAT", "State -> FINISHED")
    }
  }
}
