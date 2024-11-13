plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.plugin.parcelize)
    alias(libs.plugins.com.google.devtools.ksp)
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
        javaCompileOptions {
            annotationProcessorOptions {
                arguments["room.schemaLocation"] = "/Users/bear/AndroidStudioProjects/FilmSearcher/app/src/main/java/com/makashovadev/filmsearcher/schemas".toString()
                //arguments["room.schemaLocation"] += "${projectDir.path}/schemas".toString()
            }
        }

       // javaCompileOptions {
       //     annotationProcessorOptions {
       //         arguments ["room.schemaLocation"] += "$projectDir/schemas".toString()
//}
       // }
    }
    buildFeatures{
        viewBinding = true
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

    // DAGGER
    // Dagger2
    implementation(libs.dagger)
    // Dagger-android-processor
    implementation(libs.dagger.android.processor)
    // Dagger-android
    implementation(libs.dagger.android)
    // Dagger-android-support
    implementation(libs.dagger.android.support)
    implementation(libs.androidx.swiperefreshlayout)
    // Dagger-compiler
    ksp(libs.dagger.compiler)

    // ROOM
    implementation(libs.room.runtime)
    implementation(libs.androidx.adapters)
    //  coroutines + room
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)

    implementation(libs.lifecycle.viewmodel)
    implementation(libs.lifecycle.extensions)
    implementation(libs.lifecycle.viewmodel.ktx)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}