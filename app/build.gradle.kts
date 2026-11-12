import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    // Adicione esta linha para o Room funcionar (se der erro aqui, veja a nota abaixo sobre o TOML)
    alias(libs.plugins.ksp)
}

val courseFirebase = Properties().apply {
    val config = rootProject.file("firebase.properties")
    if (config.exists()) config.inputStream().use { load(it) }
}
fun firebaseString(key: String) = "\"" + courseFirebase.getProperty(key, "")
    .replace("\\", "\\\\").replace("\"", "\\\"") + "\""

configurations.all {
    resolutionStrategy.force(
        "androidx.compose.material3:material3:1.3.0",
        "androidx.compose.material3:material3-android:1.3.0"
    )
}

android {
    // ✅ CORREÇÃO 1: Seu namespace real
    namespace = "com.scarletstudio.ajudantedecostura"
    compileSdk = 36
    buildToolsVersion = "36.0.0"
    ndkVersion = "27.2.12479018"

    defaultConfig {
        // ✅ CORREÇÃO 2: Seu ID real
        applicationId = "com.scarletstudio.ajudantedecostura"
        minSdk = 26
        targetSdk = 36
        versionCode = 67
        versionName = "67.0"
        buildConfigField("String", "COURSES_API_KEY", firebaseString("apiKey"))
        buildConfigField("String", "COURSES_PROJECT_ID", firebaseString("projectId"))
        buildConfigField("String", "COURSES_APP_ID", firebaseString("applicationId"))

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
        buildConfig = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlin {
        compilerOptions {
            jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17
        }
    }

    packaging {
        jniLibs {
            useLegacyPackaging = false
        }
    }
}

dependencies {
    implementation(platform("com.google.firebase:firebase-bom:33.7.0"))
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-firestore")
    val composeBom = platform(libs.androidx.compose.bom)
    implementation(composeBom)
    androidTestImplementation(composeBom)

    // Core & Lifecycle
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.fragment.ktx)
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.7")
    implementation(libs.androidx.activity.compose)

    // UI Compose
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.material.icons.extended)

    // CameraX (medição de tecidos)
    implementation(libs.androidx.camera.core)
    implementation(libs.androidx.camera.camera2)
    implementation(libs.androidx.camera.lifecycle)
    implementation(libs.androidx.camera.view)
    implementation(libs.androidx.ui.tooling.preview)
    debugImplementation(libs.androidx.ui.tooling)

    // Permissões (câmera)
    implementation(libs.accompanist.permissions)

    // ✅ CORREÇÃO 3: Navigation Compose (Correção do erro 'Unresolved reference: navigation')
    implementation(libs.androidx.navigation.compose)

    // ✅ CORREÇÃO 4: Room Database (Correção dos erros 'Dao', 'Entity')
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    // Google Mobile Ads SDK — versão gerenciada pelo catálogo de versões.
    implementation(libs.play.services.ads)

    // OpenCV Local
    implementation(project(":opencv"))

    // Testes
    testImplementation(libs.junit)
    testImplementation("org.json:json:20240303")
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)

    // Guava (Obrigatório para ProcessCameraProvider)
    implementation("com.google.guava:guava:33.0.0-android")
}
