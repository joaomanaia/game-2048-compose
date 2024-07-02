import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
            freeCompilerArgs.add("-opt-in=kotlin.RequiresOptIn")
        }
    }

    jvm("desktop")

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        moduleName = "composeApp"
        browser {
            commonWebpackConfig {
                outputFileName = "composeApp.js"
            }
        }
        binaries.executable()
    }

    applyDefaultHierarchyTemplate()

    jvmToolchain(17)

    sourceSets {
        commonMain {
            kotlin.srcDir("build/generated/ksp/metadata/commonMain/kotlin")

            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.materialIconsExtended)
                implementation(compose.ui)
                implementation(compose.components.uiToolingPreview)
                implementation(compose.components.resources)

                implementation(libs.androidx.constraintlayout.compose)

                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.kotlinx.serialization.json)

                implementation(libs.androidx.navigation.compose)

                // lifecycle runtime compose causing problems
//                implementation(libs.androidx.lifecycle.runtime.compose)
                implementation(libs.androidx.lifecycle.viewmodel.compose)

                api(project.dependencies.platform(libs.koin.bom))
                api(libs.koin.core)
                implementation(libs.koin.compose)
                implementation(libs.koin.compose.viewmodel)

                implementation(libs.androidx.compose.material3.windowSizeClass)

                implementation(libs.slf4j.api)
                implementation(libs.slf4j.simple)
                implementation(libs.kotlinLogging)

                // Generate dynamic color scheme
                implementation(libs.materialKolor)
            }
        }

        commonTest.dependencies {
            implementation(kotlin("test"))
//            implementation(kotlin("test-junit5"))
            implementation(libs.junit.jupiter.params)
            implementation(libs.assertk)

            implementation(libs.kotlinx.coroutines.test)
        }

        val androidJvmMain by creating {
            dependsOn(commonMain.get())

            dependencies {
                implementation(libs.androidx.datastore.preferences)
            }
        }

        androidMain {
            dependsOn(androidJvmMain)

            dependencies {
                implementation(libs.androidx.compose.ui.tooling.preview)
                implementation(libs.androidx.activity.compose)
                implementation(libs.androidx.core.splashscreen)

                implementation(libs.kotlinx.coroutines.android)

                implementation(libs.google.material)

                implementation(libs.koin.android)
            }
        }

        val desktopMain by getting {
            dependsOn(androidJvmMain)

            dependencies {
                implementation(compose.desktop.currentOs)

                implementation(libs.koin.logger.slf4j)

                implementation(libs.kotlinx.coroutines.swing)
            }
        }
    }
}

composeCompiler {
    enableStrongSkippingMode = true
}

tasks.withType<Test> {
    useJUnitPlatform()
}

android {
    namespace = "com.joaomanaia.game2048"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.joaomanaia.game2048"
        minSdk = 21
        targetSdk = 34
        versionCode = 2
        versionName = "2.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            isDebuggable = false
            proguardFiles(
                getDefaultProguardFile("proguard-android.txt"),
                "proguard-compose-desktop.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        compose = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    dependencies {
        debugImplementation(compose.uiTooling)
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "Game 2048"
            packageVersion = "2.0.0"

            buildTypes.release {
                proguard {
                    configurationFiles.from(project.file("compose-desktop.pro"))
                    obfuscate = true
                    optimize = true
                }
            }
        }
    }
}
