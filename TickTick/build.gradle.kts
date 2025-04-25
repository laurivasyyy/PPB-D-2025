// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.9.2" apply false
    id("com.android.library") version "8.9.2" apply false
    id("org.jetbrains.kotlin.android") version "1.9.10" apply false  // Make sure Kotlin version is correct
    id("org.jetbrains.kotlin.kapt") version "1.9.10" apply false  // If using kapt
    id("com.google.devtools.ksp") version "1.9.0-1.0.13" apply false  // Ensure correct KSP version
}


tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}

// Fix for duplicate annotations class
subprojects {
    configurations.all {
        resolutionStrategy {
            force("org.jetbrains:annotations:23.0.0")
            exclude(group = "com.intellij", module = "annotations")
        }
    }
}