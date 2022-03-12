package com.infinitepower.game2048.buildsrc

object Compose {
    const val composeVersion = "1.2.0-alpha05"

    private const val composeMaterial3Version = "1.0.0-alpha07"
    const val composeMaterial3 = "androidx.compose.material3:material3:$composeMaterial3Version"

    private const val composeMaterialVersion = "1.2.0-alpha04"
    const val composeMaterial = "androidx.compose.material:material:$composeMaterialVersion"

    private const val uiToolingVersion = composeVersion
    const val uiTooling = "androidx.compose.ui:ui-tooling:$uiToolingVersion"

    private const val uiTestManifestVersion = composeVersion
    const val uiTestManifest = "androidx.compose.ui:ui-test-manifest:$uiTestManifestVersion"

    private const val composeUiVersion = composeVersion
    const val composeUi = "androidx.compose.ui:ui:$composeUiVersion"

    private const val composeUiToolingPreviewVersion = composeVersion
    const val composeUiToolingPreview = "androidx.compose.ui:ui-tooling-preview:$composeUiVersion"

    const val iconsExtended = "androidx.compose.material:material-icons-extended:$composeVersion"

    private const val constraintLayoutVersion = "1.0.0"
    const val constraintLayout = "androidx.constraintlayout:constraintlayout-compose:$constraintLayoutVersion"
}