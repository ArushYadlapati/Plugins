package dev.frozenmilk.publishing

import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging
import org.gradle.api.provider.ValueSource
import org.gradle.process.ExecOperations
import java.io.ByteArrayOutputStream
import javax.inject.Inject

abstract class GitDataValueSource : ValueSource<GitData, GitOperationValueSourceParameters> {
    @get:Inject
    abstract val execOperations: ExecOperations

    val logger: Logger = Logging.getLogger(DairyPublishingPlugin::class.java)

    override fun obtain(): GitData {
        val gitDir = parameters.gitDir.get().asFile
        val gitExecutable = parameters.gitExecutable.get()

        val clean = run {
            val sout = ByteArrayOutputStream()
            execOperations.exec {it.run {
                workingDir = gitDir
                standardOutput = sout
                commandLine(gitExecutable, "status", "--porcelain")
            }}.assertNormalExitValue()
            sout.toString().isBlank()
        }

        val tags = run {
            val out = ByteArrayOutputStream()
            execOperations.exec {
                it.run {
                    workingDir = gitDir
                    standardOutput = out
                    commandLine(gitExecutable, "tag", "--points-at", "HEAD")
                }
            }.assertNormalExitValue()
            out.toString().trim()
        }

        val ref: String
        val version: String
        val snapshot = run {
            if (tags.isBlank()) {
                val hash = run {
                    val sout = ByteArrayOutputStream()
                    execOperations.exec {
                        it.run {
                            workingDir = gitDir
                            standardOutput = sout
                            commandLine(gitExecutable, "rev-parse", "--short", "HEAD")
                        }
                    }.assertNormalExitValue()
                    sout.toString().trim()
                }.ifBlank { throw UnknownError("unable to determine hashcode for HEAD, this shouldn't be reachable") }
                ref = hash
                version = "SNAPSHOT-$hash"
                true
            } else {
                // first tag
                val tag = tags.split('\n').also {
                    if (it.size != 1) logger.warn(
                        "Found multiple tags for HEAD:\n$tags\nSelected the first one: ${
                            it.first().trim()
                        }"
                    )
                }.first().trim()
                ref = tag
                version = tag
                false
            }
        }

        return GitData(
            clean,
            ref,
            version,
            snapshot,
        )
    }
}
