## ComposeFillableLoaders

This is a port of my old library [AndroidFillableLoaders](https://github.com/JorgeCastilloPrz/AndroidFillableLoaders), which was an old port from [the iOS FillableLoaders](https://github.com/polqf/FillableLoaders) library by [Pol Quintana](https://github.com/polqf).

This library uses [Jetpack Compose dev15](https://developer.android.com/jetpack/androidx/releases/compose).

Codebase showcases the usage of `Canvas`, `Path`, and other related apis to write a frame based animation that relies on [`withFrameMillis`](https://developer.android.com/reference/kotlin/androidx/compose/dispatch/package-summary#withFrameMillis(kotlin.Function1)) to suspend waiting for each new frame to re-evaluate elapsed time and draw based on it.