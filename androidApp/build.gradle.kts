plugins {
    id("com.android.application")
    kotlin("android")
    id("org.jlleitschuh.gradle.ktlint")
}

dependencies {
    implementation(project(":shared"))
    implementation(Jetpack.Compose.ui)
    implementation(Jetpack.Compose.tooling)
    implementation(Jetpack.Compose.foundation)
    implementation(Jetpack.Compose.material)
    implementation(Jetpack.Compose.iconsExtended)
    implementation(Jetpack.navigation)
    implementation(Jetpack.Activity.activityCompose)

    implementation(Coil.core)

    implementation(Koin.viewModel)
    implementation(Koin.compose)

    implementation(Coroutines.android)
}

android {
    compileSdkVersion(Versions.compile)
    defaultConfig {
        applicationId = "com.jizoquval.chanBrowser.androidApp"
        minSdkVersion(Versions.minSdk)
        targetSdkVersion(Versions.targetSdk)
        versionCode = 1
        versionName = "1.0"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
        useIR = true
        freeCompilerArgs = listOf("-Xopt-in=kotlin.RequiresOptIn")
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Jetpack.Compose.version
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
}
