package dev.jorgecastillo.watercat

enum class WaterCatState {
  STROKE_STARTED, FILL_STARTED, FINISHED
}

data class AnimationState(
  val state: WaterCatState,
  val time: Long
)
