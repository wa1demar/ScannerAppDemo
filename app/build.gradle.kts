import com.google.protobuf.gradle.generateProtoTasks
import com.google.protobuf.gradle.protobuf
import com.google.protobuf.gradle.protoc

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
    id("androidx.navigation.safeargs.kotlin")
    kotlin("plugin.serialization")
    id("com.google.protobuf") version "0.8.17"
    id("kotlin-parcelize")
}

android {
    compileSdk = AppConfig.compileSdk
    buildToolsVersion = AppConfig.buildToolsVersion

    defaultConfig {
        applicationId = AppConfig.applicationId
        minSdk = AppConfig.minSdk
        targetSdk = AppConfig.targetSdk
        versionCode = AppConfig.versionCode
        versionName = AppConfig.versionName

        vectorDrawables.useSupportLibrary = true

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        javaCompileOptions {
            annotationProcessorOptions {
                arguments += mapOf(
                    "room.incremental" to "true",
                )
            }
        }
    }

    buildTypes {
        getByName("debug") {
            isMinifyEnabled = AppConfig.isMinifyEnabledForDebug
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        getByName("release") {
            isMinifyEnabled = AppConfig.isMinifyEnabledForRelease
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    buildFeatures {
        aidl = true
        buildConfig = true
        compose = false
        dataBinding = false
        prefab = false
        shaders = true
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
    }

    sourceSets.getByName("main") {
        java.srcDir("src/main/proto")
        java.srcDir("build/generated/source/proto/main/java")
    }
}

kapt {
    correctErrorTypes = true
}

dependencies {

    implementation("androidx.core:core-ktx:1.6.0")
    implementation("androidx.fragment:fragment-ktx:1.3.6")
    implementation("androidx.startup:startup-runtime:1.1.0")

    // Kotlin
    implementation("org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin}")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlin}")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.kotlin}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}")

    // UI
    implementation("androidx.appcompat:appcompat:1.3.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.1")
    implementation("com.google.android.material:material:1.4.0")
    implementation("androidx.recyclerview:recyclerview:1.2.1")

    // Log
    implementation("com.jakewharton.timber:timber:${Versions.timber}")

    // Hilt
    implementation("com.google.dagger:hilt-android:${Versions.hilt}")
    kapt("com.google.dagger:hilt-android-compiler:${Versions.hilt}")

    // Lifecycle
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycle}")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:${Versions.lifecycle}")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycle}")
    implementation("androidx.lifecycle:lifecycle-common-java8:${Versions.lifecycle}")

    // Navigation Component
    implementation("androidx.navigation:navigation-fragment-ktx:${Versions.navigation}")
    implementation("androidx.navigation:navigation-ui-ktx:${Versions.navigation}")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:${Versions.retrofit}")
    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:0.8.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:${Versions.serializationJson}")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.0")

    // Storage
    implementation("androidx.room:room-runtime:${Versions.room}")
    kapt("androidx.room:room-compiler:${Versions.room}")
    implementation("androidx.room:room-ktx:${Versions.room}")

    // Storage
    implementation("androidx.room:room-runtime:${Versions.room}")
    kapt("androidx.room:room-compiler:${Versions.room}")
    implementation("androidx.room:room-ktx:${Versions.room}")

    implementation("androidx.datastore:datastore-preferences:1.0.0")
    implementation("com.google.protobuf:protobuf-javalite:${Versions.protobuf}")
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:${Versions.protobuf}"
    }
    generateProtoTasks {
        all().forEach { task ->
            task.plugins {
                create("java") {
                    option("lite")
                }
            }
        }
    }
}
