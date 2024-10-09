plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.com.google.devtools.ksp)
    alias(libs.plugins.com.google.dagger.hilt.android)
}

android {
    namespace = "com.makashovadev.filmsearcher"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.makashovadev.filmsearcher"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    buildFeatures{
        viewBinding = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    //MaterialDesign
    implementation(libs.material)
    // Retrofit
    implementation(libs.retrofit2)
    // Converter Json
    implementation(libs.converter)
    // Adapter RxJava2
    implementation(libs.adapter)
    // Logging Interceptor
    implementation(libs.logging)
    // Glide
    implementation(libs.glide)
    // Dagger2
    /*implementation(libs.dagger)
    // Dagger-android-processor
    implementation(libs.dagger.android.processor)
    // Dagger-android
    implementation(libs.dagger.android)
    // Dagger-android-support
    implementation(libs.dagger.android.support)
    // Dagger-compiler
    ksp(libs.dagger.compiler)
     */

    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}