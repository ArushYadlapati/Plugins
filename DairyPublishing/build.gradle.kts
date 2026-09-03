import org.jetbrains.kotlin.gradle.dsl.JvmDefaultMode

repositories {
    mavenCentral()
    google()
}

plugins {
    id("org.jetbrains.kotlin.jvm") version "2.4.10"
    id("com.gradle.plugin-publish") version "2.1.1"
}

group = "dev.frozenmilk"
version = "0.1.0"

kotlin {
    jvmToolchain(25)
    compilerOptions {
        freeCompilerArgs.add("-Xreturn-value-checker=full")
        jvmDefault.set(JvmDefaultMode.NO_COMPATIBILITY)
    }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:2.4.10")
    implementation("org.jetbrains.dokka:dokka-gradle-plugin:2.2.0")
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
}

gradlePlugin {
    plugins {
        create("DairyPublishing") {
            id = "dev.frozenmilk.publish"
            implementationClass = "dev.frozenmilk.publishing.DairyPublishingPlugin"
        }
    }
    plugins {
        create("DairyDoc") {
            id = "dev.frozenmilk.doc"
            implementationClass = "dev.frozenmilk.doc.DairyDocPlugin"
        }
    }
}
