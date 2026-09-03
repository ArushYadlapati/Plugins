package dev.frozenmilk

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.tasks.testing.Test
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.jvm.toolchain.JavaToolchainService
import javax.inject.Inject

@Suppress("unused")
class JVMLibraryPlugin @Inject constructor(
    private val javaToolchainService: JavaToolchainService,
) : Plugin<Project> {
    override fun apply(project: Project) {
        with(project) {
            plugins.apply("java-library")
            plugins.apply("dev.frozenmilk.ftc-libraries")

            project.extensions.getByType(JavaPluginExtension::class.java).run {
                toolchain.languageVersion.set(JavaLanguageVersion.of(8))
            }

            project.tasks.withType(Test::class.java).configureEach { testTask ->
                testTask.javaLauncher.set(javaToolchainService.launcherFor {
                    it.languageVersion.set(JavaLanguageVersion.of(25))
                })
            }

            extensions.getByType(JavaPluginExtension::class.java).run {
                withSourcesJar()
            }
        }
    }
}