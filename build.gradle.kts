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

allprojects {
    repositories {
        google()
        mavenCentral()
        jcenter()
    }
}