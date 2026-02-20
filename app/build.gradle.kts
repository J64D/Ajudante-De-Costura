plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    // Adicione esta linha para o Room funcionar (se der erro aqui, veja a nota abaixo sobre o TOML)
    alias(libs.plugins.ksp)
}

android {
    // ✅ CORREÇÃO 1: Seu namespace real
    namespace = "com.scarletstudio.ajudantedecostura"
    compileSdk = 35
    ndkVersion = "27.0.12077973"

    defaultConfig {
        // ✅ CORREÇÃO 2: Seu ID real
        applicationId = "com.scarletstudio.ajudantedecostura"
        minSdk = 26
        targetSdk = 35
        versionCode = 60
        versionName = "60.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            ndk {
                debugSymbolLevel = "FULL"
            }
        }
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        // ✅ Versão segura compatível com Kotlin 1.9.24
        kotlinCompilerExtensionVersion = "1.5.14"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    packaging {
        jniLibs {
            useLegacyPackaging = false
        }
    }
}

dependencies {
    val composeBom = platform(libs.androidx.compose.bom)
    implementation(composeBom)
    androidTestImplementation(composeBom)

    // Core & Lifecycle
    implementation(libs.androidx.core.ktx)
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.4")
    implementation("androidx.activity:activity-compose:1.9.1")

    // CameraX
    val cameraVersion = "1.3.4"
    implementation("androidx.camera:camera-core:$cameraVersion")
    implementation("androidx.camera:camera-camera2:$cameraVersion")
    implementation("androidx.camera:camera-lifecycle:$cameraVersion")
    implementation("androidx.camera:camera-view:$cameraVersion")

    // Permissions
    implementation("com.google.accompanist:accompanist-permissions:0.34.0")

    // UI Compose
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.ui.tooling.preview)
    debugImplementation(libs.androidx.ui.tooling)

    // Widgets (Glance)
    implementation(libs.androidx.glance.appwidget)
    implementation(libs.androidx.glance.material3)

    // ✅ CORREÇÃO 3: Navigation Compose (Correção do erro 'Unresolved reference: navigation')
    implementation(libs.androidx.navigation.compose)

    // ✅ CORREÇÃO 4: Room Database (Correção dos erros 'Dao', 'Entity')
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    // ✅ CORREÇÃO 5: AdMob / Google Ads (Correção do erro 'MobileAds')
    implementation("com.google.android.gms:play-services-ads:23.0.0")

    // OpenCV Local
    implementation(project(":opencv"))

    // Testes
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)

    // Guava (Obrigatório para ProcessCameraProvider)
    implementation("com.google.guava:guava:33.0.0-android")
}