buildscript {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    dependencies {
        classpath(Versions.kotlinPlugin)
        classpath(Versions.gradlePlugin)
        classpath(SqlDelight.plugin)
    }
}

plugins {
    id(KtLint.plugin) version KtLint.version
}

allprojects {
    repositories {
        google()
        mavenCentral()
        jcenter()
    }
}

ktlint {
    verbose.set(true)
    outputToConsole.set(true)
}
