package dev.jorgecastillo.fillableloader

import androidx.ui.geometry.Offset
import androidx.ui.geometry.Size
import androidx.ui.graphics.ClipOp
import androidx.ui.graphics.Path
import androidx.ui.graphics.drawscope.DrawScope
import androidx.ui.graphics.drawscope.clipPath
import java.util.*
import kotlin.math.abs

var currentWave = 0

/**
 * This transform clips the path using a rectangle with the top bound mimicking the form of ocean
 * waves. The waves have a dynamic X position that also depends on the [currentFillPhase], so they
 * also move horizontally making the effect feel closer to what real world waves do.
 */
fun DrawScope.waveClip(
  currentFillPhase: Float,
  vectorSize: Size,
  waveCount: Int,
  block: DrawScope.() -> Unit
) {
  val wavesPath =
    buildWaveAtIndex(vectorSize, currentWave++ % waveCount, waveCount)
  wavesPath.shift(Offset(0f, vectorSize.height * -currentFillPhase))
  clipPath(wavesPath, ClipOp.difference, block)
}

private fun buildWaveAtIndex(originalVectorSize: Size, index: Int, waveCount: Int): Path {
  val wavesPath = Path()

  val startingHeight: Float = originalVectorSize.height - 20
  val initialOrLast = index == 1 || index == waveCount
  val xMovement: Float = originalVectorSize.width * 1f / waveCount * index
  val divisions = 8f
  var yVariation = 10f
  wavesPath.moveTo(-originalVectorSize.width, startingHeight)

  // First wave
  if (!initialOrLast) {
    yVariation = randomYVariation(originalVectorSize)
  }

  wavesPath.quadraticBezierTo(
    -originalVectorSize.width + originalVectorSize.width * 1f / divisions + xMovement,
    startingHeight + yVariation,
    -originalVectorSize.width + originalVectorSize.width * 1f / 4 + xMovement,
    startingHeight
  )
  if (!initialOrLast) {
    yVariation = randomYVariation(originalVectorSize)
  }

  wavesPath.quadraticBezierTo(
    -originalVectorSize.width + originalVectorSize.width * 1f / divisions * 3 + xMovement,
    startingHeight - yVariation,
    -originalVectorSize.width + originalVectorSize.width * 1f / 2 + xMovement,
    startingHeight
  )

  // Second wave
  if (!initialOrLast) {
    yVariation = randomYVariation(originalVectorSize)
  }

  wavesPath.quadraticBezierTo(
    -originalVectorSize.width + originalVectorSize.width * 1f / divisions * 5 + xMovement,
    startingHeight + yVariation,
    -originalVectorSize.width + originalVectorSize.width * 1f / 4 * 3 + xMovement,
    startingHeight
  )

  if (!initialOrLast) {
    yVariation = randomYVariation(originalVectorSize)
  }

  wavesPath.quadraticBezierTo(
    -originalVectorSize.width + originalVectorSize.width * 1f / divisions * 7 + xMovement,
    startingHeight - yVariation,
    -originalVectorSize.width + originalVectorSize.width + xMovement,
    startingHeight
  )

  // Third wave
  if (!initialOrLast) {
    yVariation = randomYVariation(originalVectorSize)
  }

  wavesPath.quadraticBezierTo(
    originalVectorSize.width * 1f / divisions + xMovement, startingHeight + yVariation,
    originalVectorSize.width * 1f / 4 + xMovement, startingHeight
  )

  if (!initialOrLast) {
    yVariation = randomYVariation(originalVectorSize)
  }

  wavesPath.quadraticBezierTo(
    originalVectorSize.width * 1f / divisions * 3 + xMovement, startingHeight - yVariation,
    originalVectorSize.width * 1f / 2 + xMovement, startingHeight
  )

  // Forth wave
  if (!initialOrLast) {
    yVariation = randomYVariation(originalVectorSize)
  }

  wavesPath.quadraticBezierTo(
    originalVectorSize.width * 1f / divisions * 5 + xMovement, startingHeight + yVariation,
    originalVectorSize.width * 1f / 4 * 3 + xMovement, startingHeight
  )

  if (!initialOrLast) {
    yVariation = randomYVariation(originalVectorSize)
  }

  wavesPath.quadraticBezierTo(
    originalVectorSize.width * 1f / divisions * 7 + xMovement, startingHeight - yVariation,
    originalVectorSize.width + xMovement, startingHeight
  )

  // Closing path
  wavesPath.lineTo(originalVectorSize.width + 100, startingHeight)
  wavesPath.lineTo(originalVectorSize.width + 100, 0f)
  wavesPath.lineTo(0f, 0f)
  wavesPath.close()

  return wavesPath
}

private fun randomYVariation(originalVectorSize: Size): Float {
  return nextFloat() + originalVectorSize.height * 1f / 25
}

private fun nextFloat(): Float {
  val upperBound = 10f
  val random = Random()
  return abs(random.nextFloat()) % (upperBound + 1)
}
