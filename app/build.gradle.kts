plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose.compiler)
}

android {
    namespace = "com.payu.finance"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.payu.finance"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "/META-INF/LICENSE.md"
            excludes += "/META-INF/LICENSE.txt"
            excludes += "/META-INF/NOTICE.md"
            excludes += "/META-INF/NOTICE.txt"
        }
    }
}

dependencies {
    // Kotlin
    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlin.coroutines.android)
    implementation(libs.kotlin.coroutines.core)

    // AndroidX Core
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.app.compat)
    implementation(libs.androidx.lifecycle.viewmodel)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    
    // Material Components for XML themes
    implementation(libs.google.material)

    // Compose BOM - manages all compose library versions
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.compose.material3)
    implementation(libs.compose.activity)
    implementation(libs.compose.viewmodel)
    implementation(libs.compose.lifecycle.runtime)
    implementation(libs.compose.navigation)
    
    // Compose Tooling
    debugImplementation(libs.compose.ui.tooling)

    // Network
    implementation(libs.retrofit.main)
    implementation(libs.retrofit.gson)
    implementation(platform(libs.okhttp.bom))
    implementation(libs.okhttp.main)
    implementation(libs.okhttp.logging.interceptor)
    implementation(libs.google.gson)
    
    // Chucker for network debugging
    debugImplementation(libs.chucker.library)
    releaseImplementation(libs.chucker.library.no.op)

    // Dependency Injection - Koin
    implementation(libs.koin.android)
    implementation(libs.koin.androidx.compose)

    // Image Loading - Coil
    implementation(platform(libs.coil.bom))
    implementation(libs.coil)
    implementation(libs.coil.compose)

    // Animations
    implementation(libs.lottie)
    implementation(libs.lottie.compose)

    // Lazypay Elevate
    implementation(libs.lazypay.elevate)

    // Testing
    testImplementation(libs.junit)
    testImplementation(libs.coroutine.test)
    testImplementation(libs.mockk)
    testImplementation(libs.truth)
    testImplementation(libs.turbine)
    
    androidTestImplementation(libs.junit.android)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.compose.test.junit4)
    androidTestImplementation(libs.mockk.android)
    
    debugImplementation(libs.compose.test.manifest)
}

