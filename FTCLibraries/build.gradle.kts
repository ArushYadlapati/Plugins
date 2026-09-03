import org.jetbrains.kotlin.gradle.dsl.JvmDefaultMode

repositories {
    mavenCentral()
    google()
    maven("https://repo.dairy.foundation/releases")
}

plugins {
    id("org.jetbrains.kotlin.jvm") version "2.3.0"
    id("java-gradle-plugin")
    id("dev.frozenmilk.publish")
}

group = "dev.frozenmilk"

kotlin {
    jvmToolchain(17)
    compilerOptions {
        freeCompilerArgs.add("-Xreturn-value-checker=full")
        jvmDefault.set(JvmDefaultMode.NO_COMPATIBILITY)
    }
    coreLibrariesVersion = "1.9.24"
}

dependencies {
    api("dev.frozenmilk:EasyAutoLibraries:1.2.0")
}

dairyPublishing {
    // git directory is in the parent
    gitDir = file("..")
}

gradlePlugin {
    plugins {
        create("FTCLibraries") {
            id = "dev.frozenmilk.ftc-libraries"
            implementationClass = "dev.frozenmilk.FTCLibrariesPlugin"
        }
    }
}
