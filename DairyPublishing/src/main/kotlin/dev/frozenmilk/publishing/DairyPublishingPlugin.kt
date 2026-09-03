package dev.frozenmilk.publishing

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.repositories.PasswordCredentials
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin
import org.gradle.authentication.http.BasicAuthentication

@Suppress("unused")
class DairyPublishingPlugin : Plugin<Project> {
    override fun apply(target: Project) = target.run {
        plugins.apply(MavenPublishPlugin::class.java)

        val extension = extensions.create(
            "dairyPublishing",
            DairyPublishingExtensionImpl::class.java,
            this
        )

        extension.gitDir.set(rootDir)

        extension.releasesRepository.set(uri("https://repo.dairy.foundation/releases"))
        extension.snapshotsRepository.set(uri("https://repo.dairy.foundation/snapshots"))

        val token = providers.environmentVariable("DAIRY_TOKEN")
        val password = providers.environmentVariable("DAIRY_PASSWORD")

        afterEvaluate {
            extension.finalize()
            project.version = extension.version
            val version = extension.version

            tasks.register("displayVersion") { task ->
                task.group = "Help"
                task.doLast { println(version) }
            }

            extensions.getByType(PublishingExtension::class.java).run {
                repositories.run {
                    maven {it.run {
                        name = extension.repositoryName.get()
                        url = uri(
                            if (extension.snapshot) extension.snapshotsRepository.get()
                            else extension.releasesRepository.get()
                        )

                        if (token.isPresent && password.isPresent) {
                            credentials { credentials ->
                                credentials.username = token.get()
                                credentials.password = password.get()
                            }
                        }
                        else credentials(PasswordCredentials::class.java)
                        authentication.create("basic", BasicAuthentication::class.java)
                    }}
                }
            }
        }
    }
}