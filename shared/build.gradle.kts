import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("com.squareup.sqldelight")
    kotlin("plugin.serialization") version Versions.kotlin
    id("org.jlleitschuh.gradle.ktlint")
}

// todo remove workaround after update kmm plugin to 1.5.5
android {
    configurations {
        create("androidTestApi")
        create("androidTestDebugApi")
        create("androidTestReleaseApi")
        create("testApi")
        create("testDebugApi")
        create("testReleaseApi")
    }
}

kotlin {
    ios {
        binaries {
            framework {
                baseName = "shared"
            }
        }
    }
    android {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(SqlDelight.runtime)
                implementation(SqlDelight.coroutinesExtensions)
                implementation(Coroutines.core)
                implementation(Serialization.core)
                implementation(Ktor.core)
                implementation(Ktor.serialization)
                implementation(Ktor.logger)
                implementation(Ktor.loggerLogback)
                implementation(Koin.core)
                implementation(Mvi.core)
                implementation(Mvi.defaultStorageFactory)
                implementation(Mvi.coroutineExt)
                implementation(Mvi.rx)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(Jetpack.ViewModel.compose)
                implementation(SqlDelight.androidDriver)
                implementation(Ktor.android)
            }
        }
        val androidTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
                implementation(Test.junit)
            }
        }
        val iosMain by getting {
            dependencies {
                implementation(SqlDelight.nativeDriver)
                implementation(Ktor.ios)
            }
        }
    }
}

android {
    compileSdkVersion(Versions.compile)
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdkVersion(Versions.minSdk)
        targetSdkVersion(Versions.targetSdk)
    }
}

sqldelight {
    database("AppDatabase") {
        packageName = "com.jizoquval.chanBrowser.shared.cache"
    }
}

val packForXcode by tasks.creating(Sync::class) {
    group = "build"
    val mode = System.getenv("CONFIGURATION") ?: "DEBUG"
    val sdkName = System.getenv("SDK_NAME") ?: "iphonesimulator"
    val targetName = "ios" + if (sdkName.startsWith("iphoneos")) "Arm64" else "X64"
    val framework =
        kotlin.targets.getByName<KotlinNativeTarget>(targetName).binaries.getFramework(mode)
    inputs.property("mode", mode)
    dependsOn(framework.linkTask)
    val targetDir = File(buildDir, "xcode-frameworks")
    from({ framework.outputDirectory })
    into(targetDir)
}

tasks.getByName("build").dependsOn(packForXcode)
