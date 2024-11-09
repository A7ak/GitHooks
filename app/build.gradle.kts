plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.githooks"
    compileSdk = 34

    project.tasks["preBuild"].dependsOn("copyPrepareCommitMsgHook")

    defaultConfig {
        applicationId = "com.example.githooks"
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
}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
}

tasks.register<Copy>("copyPrepareCommitMsgHook") {
    description = "copy prepare commit msg hook to git hook"
    group = "git hooks"
    outputs.upToDateWhen { false }
    from( "$rootDir/scripts/prepare-commit-msg")
    into( "$rootDir/.git/hooks/")
}

tasks.register("setExecutablePermission") {
    doLast {
        val processBuilder = ProcessBuilder("chmod", "+x", "$rootDir/.git/hooks/prepare-commit-msg")
        val process = processBuilder.start()
        val exitCode = process.waitFor()

        if (exitCode == 0) {
            println("Executable permissions set for prepare-commit-msg")
        } else {
            println("Failed to set permissions for prepare-commit-msg")
        }
    }
}

