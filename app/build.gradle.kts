plugins {
    id("com.android.application")
}

android {
    namespace = "algonquin.cst2335.testsun"
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

}