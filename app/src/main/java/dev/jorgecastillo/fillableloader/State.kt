package dev.jorgecastillo.fillableloader

enum class AnimationPhase {
  STROKE_STARTED, FILL_STARTED, FINISHED
}

data class AnimationState(
  val animationPhase: AnimationPhase,
  val elapsedTime: Long
)
