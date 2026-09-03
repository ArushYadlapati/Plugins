package dev.frozenmilk

import com.android.build.api.variant.AndroidComponentsExtension
import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.tasks.testing.Test
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.jvm.toolchain.JavaToolchainService
import javax.inject.Inject

@Suppress("unused")
class TeamCodePlugin @Inject constructor(
    private val javaToolchainService: JavaToolchainService
) : Plugin<Project> {
    override fun apply(project: Project) {
        project.plugins.apply("com.android.application")
        project.plugins.apply("dev.frozenmilk.ftc-libraries")

        val ftc = project.extensions.getByType(FTC::class.java)

        project.extensions.getByType(JavaPluginExtension::class.java).run {
            toolchain.languageVersion.set(JavaLanguageVersion.of(8))
        }

        project.tasks.withType(Test::class.java).configureEach { testTask ->
            testTask.javaLauncher.set(javaToolchainService.launcherFor {
                it.languageVersion.set(JavaLanguageVersion.of(25))
            })
        }

        val androidComponentsExtension =
            project.extensions.getByType(AndroidComponentsExtension::class.java)

        if (androidComponentsExtension !is ApplicationAndroidComponentsExtension) {
            error("TeamCode can only be applied to an Android Application")
        }

        androidComponentsExtension.finalizeDsl { it ->
            val _ = it.apply {
                namespace = namespace ?: "org.firstinspires.ftc.teamcode"

                compileSdk = 30

                signingConfigs {
                    create("release") {
                        val apkStoreFile = System.getenv("APK_SIGNING_STORE_FILE")
                        if (apkStoreFile != null) {
                            it.keyAlias = System.getenv("APK_SIGNING_KEY_ALIAS")
                            it.keyPassword = System.getenv("APK_SIGNING_KEY_PASSWORD")
                            it.storeFile = project.file(System.getenv("APK_SIGNING_STORE_FILE"))
                            it.storePassword = System.getenv("APK_SIGNING_STORE_PASSWORD")
                        } else {
                            it.keyAlias = "androiddebugkey"
                            it.keyPassword = "android"
                            it.storeFile = project.file("libs/ftc.debug.keystore")
                            it.storePassword = "android"
                        }
                    }

                    getByName("debug") {
                        it.keyAlias = "androiddebugkey"
                        it.keyPassword = "android"
                        it.storeFile = project.file("libs/ftc.debug.keystore")
                        it.storePassword = "android"
                    }
                }

                defaultConfig {
                    signingConfig = signingConfigs.getByName("debug")
                    applicationId = "com.qualcomm.ftcrobotcontroller"
                    minSdk = 24
                    targetSdk = 28

                    val (code, name) = ftc.sdk.androidVersionCodeAndName(ftc.sdk.version)
                    versionCode = code
                    versionName = name
                }

                buildTypes {
                    release {
                        signingConfig = signingConfigs.getByName("release")

                        ndk {
                            abiFilters.add("armeabi-v7a")
                            abiFilters.add("arm64-v8a")
                        }
                    }
                    debug {
                        isDebuggable = true
                        isJniDebuggable = true

                        ndk {
                            abiFilters.add("armeabi-v7a")
                            abiFilters.add("arm64-v8a")
                        }
                    }
                }

                compileOptions {
                    sourceCompatibility = JavaVersion.VERSION_1_8
                    targetCompatibility = JavaVersion.VERSION_1_8
                }

                packaging {
                    jniLibs.pickFirsts.add("**/*.so")
                    jniLibs.useLegacyPackaging = true
                }

                ndkVersion = "21.3.6528147"
            }
        }
    }
}