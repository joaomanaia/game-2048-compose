import com.infinitepower.game2048.buildsrc.ProjectConfig
import com.infinitepower.game2048.buildsrc.AndroidX
import com.infinitepower.game2048.buildsrc.Testing
import com.infinitepower.game2048.buildsrc.Material
import com.infinitepower.game2048.buildsrc.Compose

plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
}

android {
    namespace = "com.infinitepower.game2048.core"
    compileSdk = ProjectConfig.compileSdk

    defaultConfig {
        minSdk = ProjectConfig.minSdk
        targetSdk = ProjectConfig.targetSdk

        testInstrumentationRunner = ProjectConfig.testInstrumentationRunner
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
        freeCompilerArgs = listOf("-Xopt-in=kotlin.RequiresOptIn")
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Compose.composeVersion
    }
    packagingOptions {
        exclude("META-INF/AL2.0")
        exclude("META-INF/LGPL2.1")
    }
}

dependencies {
    implementation(AndroidX.coreKtx)
    implementation(AndroidX.lifecycleRuntimeKtx)

    testImplementation(Testing.junit)
    testImplementation(Testing.junitJupiter)
    androidTestImplementation(Testing.junitAndroidExt)
    androidTestImplementation(Testing.espressoCore)
    androidTestImplementation(Testing.composeUiTestJunit4)

    debugImplementation(Compose.uiTooling)
    debugImplementation(Compose.uiTestManifest)
    implementation(Compose.composeUi)
    implementation(Compose.composeUiToolingPreview)

    implementation(Material.material)

    implementation(Compose.composeMaterial3)
}