package dev.jorgecastillo.watercat

import androidx.animation.LinearOutSlowInEasing
import androidx.compose.*
import androidx.lifecycle.whenStarted
import androidx.ui.core.LifecycleOwnerAmbient
import androidx.ui.core.Modifier
import androidx.ui.foundation.Canvas
import androidx.ui.foundation.drawBackground
import androidx.ui.geometry.Size
import androidx.ui.graphics.Color
import androidx.ui.graphics.drawscope.*
import androidx.ui.graphics.vector.PathParser
import androidx.ui.layout.fillMaxSize
import kotlin.math.max
import kotlin.math.min

private const val strokeDrawingDuration = 1000
private const val fillDuration = 10000
private const val totalTime = strokeDrawingDuration + fillDuration

val animationEasing = LinearOutSlowInEasing

@Composable
fun WaterCat(originalVectorSize: Size) {
  val state = animationTimeMillis()

  fun keepDrawing(elapsedTime: Long): Boolean = elapsedTime < totalTime

  fun DrawScope.drawStroke(elapsedTime: Long) {
    val strokePercent = max(0f, min(1f, elapsedTime * 1f / strokeDrawingDuration))

    val nodesToDraw = (animationEasing(strokePercent) * catPathNodes().size).toInt()

    val path = PathParser().addPathNodes(
      catPathNodes().take(nodesToDraw)
    ).toPath()
    this.drawPath(path, Color.Blue, style = Stroke(4f))
  }

  fun DrawScope.drawFilling(elapsedTime: Long) {
    // Is stoke completely drawn.
    if (elapsedTime > strokeDrawingDuration) {
      if (state.value.animationPhase < AnimationPhase.FILL_STARTED) {
        state.value = state.value.copy(animationPhase = AnimationPhase.FILL_STARTED)
      }

      val fillPercent =
        max(0f, min(1f, (elapsedTime - strokeDrawingDuration) / fillDuration.toFloat()))

      plainClip(fillPercent, originalVectorSize) {
        drawPath(catPath(), Color.Blue, style = Fill)
      }
    }
  }

  Canvas(modifier = Modifier.fillMaxSize() + Modifier.drawBackground(Color.Magenta)) {
    val originalCanvasWidth = size.width
    val originalCanvasHeight = size.height
    val scaleFactor = originalCanvasWidth / originalVectorSize.width

    scale(
      scaleX = scaleFactor,
      scaleY = scaleFactor
    ) {
      translate(
        left = originalCanvasWidth / 2f - originalVectorSize.width / 2f,
        top = originalCanvasHeight / 2f - originalVectorSize.height / 2f
      ) {

        val elapsedTime = state.value.elapsedTime
        drawStroke(elapsedTime)
        drawFilling(elapsedTime)

        if (!keepDrawing(elapsedTime)) {
          state.value = state.value.copy(animationPhase = AnimationPhase.FINISHED)
        }
      }
    }
  }
}

/**
 * Returns a [MutableState] holding a local animation time in milliseconds plus the current
 * [AnimationPhase]. The local animation time always starts at `0L` and stops updating when the call
 * leaves the composition. The animation phase starts as [AnimationPhase.STROKE_STARTED], since
 * stroke always renders first.
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
