package dev.frozenmilk.publishing

import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.api.publish.plugins.PublishingPlugin
import org.gradle.api.reflect.HasPublicType
import org.gradle.api.reflect.TypeOf
import org.gradle.process.ExecOperations
import java.io.ByteArrayOutputStream
import java.net.URI
import javax.inject.Inject

@Suppress("MemberVisibilityCanBePrivate")
abstract class DairyPublishingExtensionImpl @Inject constructor(
    private val project: Project,
    private val exec: ExecOperations,
) : DairyPublishingExtension, HasPublicType {
    override fun getPublicType(): TypeOf<DairyPublishingExtension> =
        TypeOf.typeOf(DairyPublishingExtension::class.java)

    /**
     * the directory that contains the `.git` directory, by default this is the root project directory
     */
    override val gitDir: DirectoryProperty = project.objects.directoryProperty()

    /**
     * the name of the `git` executable, by default this is "git"
     */
    override val gitExecutable: Property<String> =
        project.objects.property(String::class.java).apply { set("git") }

    /**
     * the repository name, by default this is "Dairy"
     */
    override val repositoryName: Property<String> =
        project.objects.property(String::class.java).apply { set("Dairy") }

    /**
     * the uri of the releases repository, by default this is the dairy releases repository
     */
    override val releasesRepository: Property<URI> = project.objects.property(URI::class.java)

    /**
     * the uri of the snapshots repository, by default this is the dairy snapshots repository
     */
    override val snapshotsRepository: Property<URI> = project.objects.property(URI::class.java)

    private val configurationActions = mutableListOf<Action<DairyPublishingExtensionImpl>>()

    private val gitData = project.providers.of(GitDataValueSource::class.java) {
        it.parameters.gitDir.set(gitDir)
        it.parameters.gitExecutable.set(gitExecutable)
    }

    override val gitRef: String
        get() {
            finalize()
            return gitData.get().ref
        }

    override val version: String
        get() {
            finalize()
            return gitData.get().version
        }

    override val clean: Boolean
        get() {
            finalize()
            return gitData.get().clean
        }

    override val snapshot: Boolean
        get() {
            finalize()
            return gitData.get().snapshot
        }

    /**
     * adds an action to run before this is consumed, actions are run in order of registration
     */
    fun configureBeforeConsume(action: Action<DairyPublishingExtensionImpl>) {
        configurationActions.add(action)
    }

    private var finalised = false
    internal fun finalize() {
        if (finalised) return
        finalised = true
        // NOTE: allows for configuration actions register themselves
        var i = 0
        while (i < configurationActions.size) {
            configurationActions[i].execute(this)
            i++
        }
        gitDir.finalizeValue()
        gitExecutable.finalizeValue()
        repositoryName.finalizeValue()
        releasesRepository.finalizeValue()
        snapshotsRepository.finalizeValue()
        gitData.get()

        if (!clean) project.tasks.all {
            if (it.group == PublishingPlugin.PUBLISH_TASK_GROUP) {
                it.doFirst {
                    throw UncleanWorkingTree()
                }
            }
        }
    }
}