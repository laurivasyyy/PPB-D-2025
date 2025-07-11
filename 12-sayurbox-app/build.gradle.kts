plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false

    // Use stable Hilt plugin without KSP
    id("com.google.dagger.hilt.android") version "2.48" apply false
}