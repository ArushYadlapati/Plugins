package dev.frozenmilk

import com.android.build.api.variant.AndroidComponentsExtension
import com.android.build.api.variant.LibraryAndroidComponentsExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.tasks.testing.Test
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.jvm.toolchain.JavaToolchainService
import javax.inject.Inject

@Suppress("unused", "DEPRECATION")
class AndroidLibraryPlugin @Inject constructor(
    private val javaToolchainService: JavaToolchainService,
) : Plugin<Project> {
    override fun apply(project: Project) {
        project.plugins.apply("com.android.library")
        project.plugins.apply("dev.frozenmilk.ftc-libraries")

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

        if (androidComponentsExtension !is LibraryAndroidComponentsExtension) error("Library can only be applied to an Android Library")

        androidComponentsExtension.finalizeDsl {
            val _ = it.apply {
                compileSdk = 30

                defaultConfig {
                    minSdk = 24
                    targetSdk = 28

                    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                }

                compileOptions {
                    sourceCompatibility = JavaVersion.VERSION_1_8
                    targetCompatibility = JavaVersion.VERSION_1_8
                }

                ndkVersion = "21.3.6528147"

                publishing {
                    singleVariant("release") {
                        withSourcesJar()
                    }
                }
            }
        }
    }
}