package com.infinitepower.game2048.buildsrc

object Testing {
    private const val junitVersion = "4.13.2"
    const val junit = "junit:junit:$junitVersion"

    private const val junitAndroidExtVersion = "1.1.3"
    const val junitAndroidExt = "androidx.test.ext:junit:$junitAndroidExtVersion"

    private const val espressoCoreVersion = "3.4.0"
    const val espressoCore = "androidx.test.espresso:espresso-core:$espressoCoreVersion"

    private const val composeUiTestJunit4Version = Compose.composeVersion
    const val composeUiTestJunit4 = "androidx.compose.ui:ui-test-junit4:$composeUiTestJunit4Version"

    private const val junitJupiterVersion = "5.8.2"
    const val junitJupiter = "org.junit.jupiter:junit-jupiter:$junitJupiterVersion"

    private const val googleTruthVersion = "1.1.3"
    const val googleTruth = "com.google.truth:truth:$googleTruthVersion"
}