import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

version = "11.2.1"

plugins {
    //noinspection AndroidGradlePluginVersion
    id("com.android.library") version "8.13.2"
    id("org.gradle.maven-publish")
    id("dev.frozenmilk.ftc-libraries")
    id("dev.frozenmilk.doc")
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(8)
}

ftc.sdk {
    version = project.version.toString()
    implementation(FtcCommon)
    implementation(RobotCore)
    implementation(RobotServer)
    implementation(Hardware)
    implementation(Vision)
    implementation(Inspection)
    implementation(Blocks)
    implementation(OnBotJava)
    implementation(appcompat)
}

android {
    defaultConfig {
        minSdk = 24
        @Suppress("DEPRECATION")
        //noinspection ExpiredTargetSdkVersion
        targetSdk = 28
        buildConfigField(
            "String",
            "APP_BUILD_TIME",
            "\"${SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.ROOT).format(Date())}\"",
        )
    }

    buildFeatures {
        buildConfig = true
    }

    //noinspection GradleDependency
    compileSdk = 30

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    namespace = "com.qualcomm.ftcrobotcontroller"
    publishing {
        singleVariant("release") {
            withSourcesJar()
        }
    }
}

publishing {
    repositories {
        maven {
            name = "Dairy"
            url = uri("https://repo.dairy.foundation/releases")
            credentials(PasswordCredentials::class)
            authentication {
                create<BasicAuthentication>("basic")
            }
        }
    }
    publications {
        register<MavenPublication>("release") {
            groupId = "com.qualcomm.ftcrobotcontroller"
            artifactId = "FtcRobotController"

            artifact(dairyDoc.dokkaHtmlJar)
            artifact(dairyDoc.dokkaJavadocJar)

            afterEvaluate {
                from(components["release"])
            }
        }
    }
}