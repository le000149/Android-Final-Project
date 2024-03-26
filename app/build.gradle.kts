plugins {
    id("com.android.application")
}

android {
    namespace = "org.algonquin.cst2355.finalproject"
    compileSdk = 34

    defaultConfig {
        applicationId = "org.algonquin.cst2355.finalproject"
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
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")


    dependencies {
        implementation ("com.android.volley:volley:1.2.0")
    }
    implementation("com.google.code.gson:gson:2.8.0")
    implementation ("com.alibaba:fastjson:1.1.55.android")
    //    images from web
    implementation ("com.github.bumptech.glide:glide:4.10.0")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.10.0")
    //indictor

    implementation ("com.github.bumptech.glide:glide:3.7.0")

}