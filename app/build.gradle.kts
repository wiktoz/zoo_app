plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.blog_app_new"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.blog_app_new"
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation("com.squareup.retrofit2:retrofit:2.9.0")  // For API calls
    implementation("com.google.code.gson:gson:2.10")        // For JSON parsing
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")  // For lifecycle-aware components
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")  // For logging network requests
    implementation("com.squareup.retrofit2:converter-gson:2.0.0-beta4")  // For network requests
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.activity)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}