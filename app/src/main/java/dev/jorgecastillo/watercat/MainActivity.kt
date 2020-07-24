package dev.jorgecastillo.watercat

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.ui.core.Modifier
import androidx.ui.core.setContent
import androidx.ui.foundation.Text
import androidx.ui.layout.Column
import androidx.ui.layout.Row
import androidx.ui.material.MaterialTheme

class MainActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      MaterialTheme {
        Column {
          Row(Modifier.weight(0.33f)) {
            Text("Something")
          }
          Row(Modifier.weight(0.33f)) {
            WaterCat(
              originalVectorWidth = 512,
              originalVectorHeight = 512
            )
          }
          Row(Modifier.weight(0.33f)) {
            Text("SomethingElse")
          }
        }
      }
    }
  }
}


