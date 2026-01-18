plugins {
    alias(libs.plugins.android.application)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.librarymanagementsystem"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.example.librarymanagementsystem"
        minSdk = 24
        targetSdk = 36
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    // This manages all your Firebase versions automatically so they don't crash each other.
    implementation(platform("com.google.firebase:firebase-bom:34.8.0"))
    // 2. Firebase Authentication (For Login: Admin vs Student)
    implementation("com.google.firebase:firebase-auth")
    // 3. Cloud Firestore (For the Database: Books & Categories)
    implementation("com.google.firebase:firebase-firestore")
}