package dev.jorgecastillo.watercat

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.ui.core.setContent
import androidx.ui.geometry.Size
import androidx.ui.material.MaterialTheme

class MainActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      MaterialTheme {
        WaterCat(
          originalVectorSize = Size(512f, 512f)
        )
      }
    }
  }
}
