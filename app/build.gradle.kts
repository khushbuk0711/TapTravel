plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("com.google.gms.google-services")
    alias(libs.plugins.googleAndroidLibrariesMapsplatformSecretsGradlePlugin)
}

android {
    namespace = "com.example.taptravel"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.taptravel"
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
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
        dataBinding = true
    }
}


dependencies {
    implementation("com.google.firebase:firebase-analytics")
    implementation ("com.google.firebase:firebase-auth")
    implementation ("com.google.code.gson:gson:2.9.0")
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.6")
    implementation("com.squareup.okhttp3:okhttp:5.0.0-alpha.11")
    implementation("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.11")
    implementation ("it.xabaras.android:recyclerview-swipedecorator:1.4")
    implementation(platform("com.google.firebase:firebase-bom:33.1.0"))
    implementation ("com.github.bumptech.glide:glide:4.16.0")
    implementation ("com.mapmyindia.sdk:mapmyindia-android-sdk:7.0.3")
//     implementation ("com.android.volley:volley:1.1.1")
    implementation ("de.hdodenhof:circleimageview:3.1.0")
    implementation ("com.google.firebase:firebase-auth-ktx:21.0.3")
    implementation ("com.squareup.picasso:picasso:2.71828")

//    implementation(libs.maps)
    annotationProcessor ("com.github.bumptech.glide:compiler:4.16.0")



    implementation("com.google.firebase:firebase-firestore")

    implementation("androidx.lifecycle:lifecycle-runtime:2.6.1")
//    implementation("com.google.android.libraries.maps:maps:3.1.0-beta")
//    implementation("com.google.android.libraries.maps:maps-ktx:3.1.0-beta")
//    implementation("com.google.android.gms:play-services-maps:18.1.0")


    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.firebase.auth.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}