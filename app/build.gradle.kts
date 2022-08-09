import com.joaomanaia.game2048.buildsrc.ProjectConfig
import com.joaomanaia.game2048.buildsrc.AndroidX
import com.joaomanaia.game2048.buildsrc.Compose
import com.joaomanaia.game2048.buildsrc.Hilt
import com.joaomanaia.game2048.buildsrc.Material
import com.joaomanaia.game2048.buildsrc.DataStore
import com.joaomanaia.game2048.buildsrc.Kotlinx

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("kotlinx-serialization")
    id("dagger.hilt.android.plugin")
}

android {
    namespace = ProjectConfig.namespace
    compileSdk = ProjectConfig.compileSdk

    defaultConfig {
        applicationId = ProjectConfig.applicationId
        minSdk = ProjectConfig.minSdk
        targetSdk = ProjectConfig.targetSdk
        versionCode = ProjectConfig.versionCode
        versionName = ProjectConfig.versionName

        testInstrumentationRunner = ProjectConfig.testInstrumentationRunner
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            isDebuggable = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
        freeCompilerArgs = listOf("-opt-in=kotlin.RequiresOptIn")
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Compose.composeCompilerVersion
    }
    packagingOptions {
        exclude("META-INF/AL2.0")
        exclude("META-INF/LGPL2.1")
    }
}

dependencies {
    implementation(AndroidX.coreKtx)
    implementation(AndroidX.lifecycleRuntimeKtx)

    implementation(AndroidX.activityCompose)

    implementation(Compose.composeMaterial3)
    debugImplementation(Compose.uiTooling)
    debugImplementation(Compose.uiTestManifest)
    implementation(Compose.composeUi)
    implementation(Compose.composeUiToolingPreview)
    implementation(Compose.iconsExtended)
    implementation(Compose.constraintLayout)

    implementation(Hilt.hiltAndroid)
    kapt(Hilt.hiltCompiler)
    kapt(Hilt.androidXHiltCompiler)
    implementation(Hilt.navigationCompose)

    implementation(Material.material)

    implementation(Kotlinx.serialization)

    implementation(DataStore.dataStorePreferences)
}