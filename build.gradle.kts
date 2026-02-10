// Project-level build.gradle.kts
plugins {
    // 既存の libs.versions.toml を活かします
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false

    // KSP: Roomデータベースを動かすために必須です
    id("com.google.devtools.ksp") version "1.9.22-1.0.17" apply false
}