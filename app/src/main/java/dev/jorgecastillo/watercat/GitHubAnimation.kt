package dev.jorgecastillo.watercat

import android.util.Log
import androidx.animation.LinearOutSlowInEasing
import androidx.compose.*
import androidx.lifecycle.whenStarted
import androidx.ui.core.LifecycleOwnerAmbient
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

private const val strokeDrawingDuration = 1000
private const val fillDuration = 10000

val animationEasing = LinearOutSlowInEasing

@Composable
fun WaterCat() {
  val state = animationTimeMillis()

  fun getFillPercent(elapsedTime: Long): Float {
    return max(0f, min(1f, (elapsedTime - strokeDrawingDuration) / fillDuration.toFloat()))
  }

  fun keepDrawing(elapsedTime: Long): Boolean =
    elapsedTime < strokeDrawingDuration + fillDuration

  fun DrawScope.drawStroke(elapsedTime: Long) {
    val strokePercent = max(0f, min(1f, elapsedTime * 1f / strokeDrawingDuration))

    val amountOfNodesToDraw = (animationEasing(
      strokePercent
    ) * catPathNodes().size).toInt()

    val path = PathParser().addPathNodes(
      catPathNodes().take(amountOfNodesToDraw)
    ).toPath()
    this.drawPath(path, Color.Blue, style = Stroke(4f))
  }

  Canvas(modifier = Modifier.fillMaxSize()) {
    val elapsedTime = state.value.elapsedTime
    drawStroke(elapsedTime)

    // Is stoke completely drawn.
    if (elapsedTime > strokeDrawingDuration) {
      if (state.value.animationPhase < AnimationPhase.FILL_STARTED) {
        state.value = state.value.copy(animationPhase = AnimationPhase.FILL_STARTED)
        Log.d("CAT", "Elapsed: $elapsedTime, State -> FILL_STARTED")
      }

      val fillPercent = getFillPercent(elapsedTime)
      plainClip(fillPercent) {
        drawPath(catPath(), Color.Blue, style = Fill)
      }
    }

    if (!keepDrawing(elapsedTime)) {
      state.value = state.value.copy(animationPhase = AnimationPhase.FINISHED)
      Log.d("CAT", "State -> FINISHED")
    }
  }
}

/**
 * Returns a [State] holding a local animation time in milliseconds. The value always starts
 * at `0L` and stops updating when the call leaves the composition.
 */
@Composable
private fun animationTimeMillis(): MutableState<AnimationState> {
  val millisState = state { AnimationState(AnimationPhase.STROKE_STARTED, 0L) }
  val lifecycleOwner = LifecycleOwnerAmbient.current
  launchInComposition {
    val startTime = awaitFrameMillis { it }
    lifecycleOwner.whenStarted {
      while (true) {
        awaitFrameMillis { frameTime ->
          millisState.value = millisState.value.copy(elapsedTime = frameTime - startTime)
        }
      }
    }
  }
  return millisState
}
