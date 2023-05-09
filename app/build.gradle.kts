import com.joaomanaia.game2048.buildsrc.ProjectConfig
import de.fayard.refreshVersions.core.versionFor

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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
        freeCompilerArgs = listOf("-opt-in=kotlin.RequiresOptIn")
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = versionFor(AndroidX.compose.compiler)
    }
    packagingOptions {
        exclude("META-INF/AL2.0")
        exclude("META-INF/LGPL2.1")
    }
}

dependencies {
    implementation(AndroidX.core.ktx)
    implementation(AndroidX.lifecycle.runtime.ktx)

    implementation(AndroidX.activity.compose)

    implementation(AndroidX.compose.ui.tooling)
    implementation(AndroidX.compose.ui.toolingPreview)
    implementation(AndroidX.activity.compose)
    implementation(AndroidX.compose.material3)
    implementation(AndroidX.compose.material.icons.extended)
    implementation(AndroidX.constraintLayout.compose)

    implementation(Google.dagger.hilt.android)
    kapt(Google.dagger.hilt.compiler)
    kapt(AndroidX.hilt.compiler)
    implementation(AndroidX.hilt.navigationCompose)

    implementation(Google.android.material)

    implementation(KotlinX.serialization.json)

    implementation(AndroidX.dataStore.preferences)
}