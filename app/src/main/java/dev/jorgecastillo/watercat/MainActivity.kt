package dev.jorgecastillo.watercat

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.ui.core.Modifier
import androidx.ui.core.setContent
import androidx.ui.foundation.Box
import androidx.ui.foundation.drawBackground
import androidx.ui.geometry.Size
import androidx.ui.graphics.Color
import androidx.ui.layout.fillMaxSize
import androidx.ui.material.MaterialTheme

class MainActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      MaterialTheme {
        Box(Modifier.fillMaxSize() + Modifier.drawBackground(Color.DarkGray)) {
          WaterCat(
            originalVectorSize = Size(512f, 512f),
            strokeColor = Color.White,
            fillColor = Color.Magenta,
            strokeDurationMillis = 2000,
            fillDurationMillis = 8000
          )
        }
      }
    }
  }
}
