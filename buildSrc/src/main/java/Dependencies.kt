object Versions {
    const val minSdk = 24
    const val compile = 30
    const val targetSdk = 30
    const val kotlin = "1.4.31"

    private const val gradleVersion = "7.0.0-alpha07"

    const val kotlinPlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin"
    const val gradlePlugin = "com.android.tools.build:gradle:$gradleVersion"
}

object KtLint {
    const val version = "10.0.0"
    const val plugin = "org.jlleitschuh.gradle.ktlint"
}

object Ktor {
    private const val version = "1.5.1"

    const val core = "io.ktor:ktor-client-core:$version"
    const val serialization = "io.ktor:ktor-client-serialization:$version"
    const val logger = "io.ktor:ktor-client-logging:$version"
    const val android = "io.ktor:ktor-client-android:$version"
    const val ios = "io.ktor:ktor-client-ios:$version"
    const val loggerLogback = "ch.qos.logback:logback-classic:1.2.3"
}

object Koin {
    private const val version = "3.0.1-beta-1"

    const val core = "io.insert-koin:koin-core:$version"
    const val compose = "io.insert-koin:koin-androidx-compose:$version"
    const val viewModel = "io.insert-koin:koin-android:$version"
}

object Jetpack {

    const val navigation = "androidx.navigation:navigation-compose:1.0.0-alpha08"

    object Compose {
        const val version = "1.0.0-beta03"

        const val material = "androidx.compose.material:material:$version"
        const val tooling = "androidx.compose.ui:ui-tooling:$version"
        const val ui = "androidx.compose.ui:ui:$version"
        const val foundation = "androidx.compose.foundation:foundation:$version"
        const val iconsExtended = "androidx.compose.material:material-icons-extended:$version"
    }

    object ViewModel {
        private const val version = "1.0.0-alpha03"

        const val compose = "androidx.lifecycle:lifecycle-viewmodel-compose:$version"
    }

    object Activity {
        private const val version = "1.3.0-alpha03"

        const val activityCompose = "androidx.activity:activity-compose:$version"
    }
}

object Mvi {
    private const val version = "2.0.1"

    const val core = "com.arkivanov.mvikotlin:mvikotlin:$version"
    const val defaultStorageFactory = "com.arkivanov.mvikotlin:mvikotlin-main:$version"
    const val coroutineExt = "com.arkivanov.mvikotlin:mvikotlin-extensions-coroutines:$version"
    const val rx = "com.arkivanov.mvikotlin:rx:$version"
}

object Coroutines {
    private const val version = "1.4.2"

    const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$version"
    const val android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$version"
}

object SqlDelight {
    private const val version = "1.4.4"

    const val plugin = "com.squareup.sqldelight:gradle-plugin:$version"
    const val runtime = "com.squareup.sqldelight:runtime:$version"
    const val coroutinesExtensions = "com.squareup.sqldelight:coroutines-extensions:$version"
    const val androidDriver = "com.squareup.sqldelight:android-driver:$version"
    const val nativeDriver = "com.squareup.sqldelight:native-driver:$version"
}

object Serialization {
    private const val version = "1.1.0"

    const val core = "org.jetbrains.kotlinx:kotlinx-serialization-core:$version"
}

object Test {
    const val junit = "junit:junit:4.13.1"
}

object Coil {
    private const val version = "0.7.0"

    const val core = "com.google.accompanist:accompanist-coil:$version"
}