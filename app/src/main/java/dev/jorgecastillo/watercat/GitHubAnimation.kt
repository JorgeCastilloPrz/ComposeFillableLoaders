package dev.jorgecastillo.watercat

import androidx.animation.LinearOutSlowInEasing
import androidx.compose.*
import androidx.lifecycle.whenStarted
import androidx.ui.core.LifecycleOwnerAmbient
import androidx.ui.core.Modifier
import androidx.ui.foundation.Canvas
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
fun WaterCat(originalVectorSize: Size, strokeColor: Color, fillColor: Color) {
  val state = animationTimeMillis()

  fun DrawScope.drawStroke(elapsedTime: Long) {
    val strokePercent = max(0f, min(1f, elapsedTime * 1f / strokeDrawingDuration))

    val nodesToDraw = (animationEasing(strokePercent) * catPathNodes().size).toInt()

    val path = PathParser().addPathNodes(
      catPathNodes().take(nodesToDraw)
    ).toPath()
    this.drawPath(path, strokeColor, style = Stroke(2f))
  }

  fun DrawScope.drawFilling(elapsedTime: Long) {
    // Is stoke completely drawn, we can start drawing the filling.
    if (elapsedTime > strokeDrawingDuration) {
      val fillPercent =
        max(0f, min(1f, (elapsedTime - strokeDrawingDuration) / fillDuration.toFloat()))

      waveClip(fillPercent, originalVectorSize, 128) {
        drawPath(catPath(), fillColor, style = Fill)
      }
    }
  }

  Canvas(modifier = Modifier.fillMaxSize()) {
    val originalCanvasWidth = size.width
    val originalCanvasHeight = size.height
    val scaleFactor = min(
      originalCanvasWidth / originalVectorSize.width,
      originalCanvasHeight / originalVectorSize.height
    )

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
      }
    }
  }
}

fun keepDrawing(elapsedTime: Long): Boolean = elapsedTime < totalTime

/**
 * Returns a [State] holding a local animation time in milliseconds and the current [AnimationPhase]
 * The local animation time always starts at `0L` and stops updating when the call
 * leaves the composition. The animation phase starts as [AnimationPhase.STROKE_STARTED], since
 * stroke always renders first, and gets updated according to the elapsed time.
 */
@Composable
private fun animationTimeMillis(): State<AnimationState> {
  val state = state { AnimationState(AnimationPhase.STROKE_STARTED, 0L) }
  val lifecycleOwner = LifecycleOwnerAmbient.current

  launchInComposition {
    val startTime = awaitFrameMillis { it }

    lifecycleOwner.whenStarted {
      while (true) {
        awaitFrameMillis { frameTime ->
          val elapsedTime = frameTime - startTime
          if (!keepDrawing(elapsedTime)) {
            state.value = state.value.copy(animationPhase = AnimationPhase.FINISHED)
          }

          if (elapsedTime > strokeDrawingDuration) {
            if (state.value.animationPhase < AnimationPhase.FILL_STARTED) {
              state.value =
                state.value.copy(animationPhase = AnimationPhase.FILL_STARTED)
            }
          }

          state.value = state.value.copy(elapsedTime = frameTime - startTime)
        }
      }
    }
  }
  return state
}
