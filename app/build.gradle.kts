plugins {
    id("com.android.application")
}

android {


    buildFeatures {
        viewBinding = true
    }

    namespace = "algonquin.cst2335.finalproject"

    compileSdk = 34

    defaultConfig {
        applicationId = "algonquin.cst2335.testsun"
        minSdk = 27
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
}




    dependencies {

        implementation ("com.squareup.retrofit2:retrofit:2.9.0")

        implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    }

    implementation ("com.android.volley:volley:1.2.0")
    implementation ("androidx.room:room-runtime:2.4.2")
    implementation("androidx.activity:activity:1.8.0")

    implementation("junit:junit:4.12") // Use the latest version available
    annotationProcessor ("androidx.room:room-compiler:2.4.2")

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("androidx.activity:activity:1.8.0")

    testImplementation("junit:junit:4.13.2")

    androidTestImplementation ("androidx.test:runner:1.4.0")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation("com.android.volley:volley:1.2.1")
    implementation("com.google.code.gson:gson:2.10.1")

    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.test.espresso:espresso-contrib:3.5.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.test.espresso:espresso-contrib:3.6.0-alpha03")
    annotationProcessor("androidx.room:room-compiler:2.6.1")
    implementation ("com.squareup.picasso:picasso:2.8")
    implementation ("com.jakewharton.threetenabp:threetenabp:1.3.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")

    implementation("androidx.recyclerview:recyclerview:1.3.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation("com.android.volley:volley:1.2.1")
    val room_version = "2.6.1"
    implementation ("androidx.room:room-runtime:$room_version")
    annotationProcessor("androidx.room:room-compiler:$room_version")

    androidTestImplementation("androidx.test.ext:junit:1.2.0-alpha03")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.0-alpha03")
    implementation ("com.alibaba:fastjson:1.1.55.android")


    implementation ("com.github.bumptech.glide:glide:4.10.0")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.10.0")

    implementation ("com.github.bumptech.glide:glide:3.7.0")


}