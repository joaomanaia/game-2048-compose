package com.infinitepower.game2048.buildsrc

object Hilt {
    private const val hiltVersion = "2.41"

    const val hiltAndroid = "com.google.dagger:hilt-android:$hiltVersion"

    const val hiltCompiler = "com.google.dagger:hilt-compiler:$hiltVersion"

    private const val androidXHiltCompilerVersion = "1.0.0"
    const val androidXHiltCompiler = "androidx.hilt:hilt-compiler:$androidXHiltCompilerVersion"

    private const val navigationComposeVersion = "1.0.0"
    const val navigationCompose = "androidx.hilt:hilt-navigation-compose:$navigationComposeVersion"

    const val hiltAndroidTesting = "com.google.dagger:hilt-android-testing:$hiltVersion"

    const val hiltAndroidCompiler = "com.google.dagger:hilt-android-compiler$hiltVersion"
}