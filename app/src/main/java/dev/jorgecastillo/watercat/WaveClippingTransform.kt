package dev.jorgecastillo.watercat

import androidx.ui.geometry.Offset
import androidx.ui.geometry.Size
import androidx.ui.graphics.ClipOp
import androidx.ui.graphics.Path
import androidx.ui.graphics.drawscope.DrawScope
import androidx.ui.graphics.drawscope.clipPath
import java.util.*

var currentWave = 0

/**
 * This transform clips the path using a rectangle with the top bound mimicking the form of ocean
 * waves. The waves have a dynamic X position that also depends on the [currentFillPhase], so they
 * also move horizontally making the effect feel closer to what real world waves do.
 */
fun DrawScope.waveClip(
  currentFillPhase: Float,
  originalVectorSize: Size,
  block: DrawScope.() -> Unit
) {
  val waveCount = 128

  val wavesPath = Path()
  buildWaveAtIndex(originalVectorSize, wavesPath, currentWave++ % waveCount, waveCount)
  wavesPath.shift(Offset(0f, originalVectorSize.height * -currentFillPhase))
  clipPath(wavesPath, ClipOp.difference, block)
}

private fun buildWaveAtIndex(
  originalVectorSize: Size,
  wavesPath: Path,
  index: Int,
  waveCount: Int
) {
  val startingHeight: Float = originalVectorSize.height - 20
  val initialOrLast = index == 1 || index == waveCount
  val xMovement: Float = originalVectorSize.width * 1f / waveCount * index
  val divisions = 8f
  var variation = 10f
  wavesPath.moveTo(-originalVectorSize.width, startingHeight)

  // First wave
  if (!initialOrLast) {
    variation = randomFloat(originalVectorSize)
  }
  wavesPath.quadraticBezierTo(
    -originalVectorSize.width + originalVectorSize.width * 1f / divisions + xMovement,
    startingHeight + variation,
    -originalVectorSize.width + originalVectorSize.width * 1f / 4 + xMovement,
    startingHeight
  )
  if (!initialOrLast) {
    variation = randomFloat(originalVectorSize)
  }
  wavesPath.quadraticBezierTo(
    -originalVectorSize.width + originalVectorSize.width * 1f / divisions * 3 + xMovement,
    startingHeight - variation,
    -originalVectorSize.width + originalVectorSize.width * 1f / 2 + xMovement,
    startingHeight
  )

  // Second wave
  if (!initialOrLast) {
    variation = randomFloat(originalVectorSize)
  }
  wavesPath.quadraticBezierTo(
    -originalVectorSize.width + originalVectorSize.width * 1f / divisions * 5 + xMovement,
    startingHeight + variation,
    -originalVectorSize.width + originalVectorSize.width * 1f / 4 * 3 + xMovement,
    startingHeight
  )
  if (!initialOrLast) {
    variation = randomFloat(originalVectorSize)
  }
  wavesPath.quadraticBezierTo(
    -originalVectorSize.width + originalVectorSize.width * 1f / divisions * 7 + xMovement,
    startingHeight - variation,
    -originalVectorSize.width + originalVectorSize.width + xMovement,
    startingHeight
  )

  // Third wave
  if (!initialOrLast) {
    variation = randomFloat(originalVectorSize)
  }
  wavesPath.quadraticBezierTo(
    originalVectorSize.width * 1f / divisions + xMovement, startingHeight + variation,
    originalVectorSize.width * 1f / 4 + xMovement, startingHeight
  )
  if (!initialOrLast) {
    variation = randomFloat(originalVectorSize)
  }
  wavesPath.quadraticBezierTo(
    originalVectorSize.width * 1f / divisions * 3 + xMovement, startingHeight - variation,
    originalVectorSize.width * 1f / 2 + xMovement, startingHeight
  )

  // Forth wave
  if (!initialOrLast) {
    variation = randomFloat(originalVectorSize)
  }
  wavesPath.quadraticBezierTo(
    originalVectorSize.width * 1f / divisions * 5 + xMovement, startingHeight + variation,
    originalVectorSize.width * 1f / 4 * 3 + xMovement, startingHeight
  )
  if (!initialOrLast) {
    variation = randomFloat(originalVectorSize)
  }
  wavesPath.quadraticBezierTo(
    originalVectorSize.width * 1f / divisions * 7 + xMovement, startingHeight - variation,
    originalVectorSize.width + xMovement, startingHeight
  )

  // Closing path
  wavesPath.lineTo(originalVectorSize.width + 100, startingHeight)
  wavesPath.lineTo(originalVectorSize.width + 100, 0f)
  wavesPath.lineTo(0f, 0f)
  wavesPath.close()
}

private fun randomFloat(originalVectorSize: Size): Float {
  return nextFloat(10f) + originalVectorSize.height * 1f / 25
}

private fun nextFloat(upperBound: Float): Float {
  val random = Random()
  return Math.abs(random.nextFloat()) % (upperBound + 1)
}

