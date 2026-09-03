package dev.frozenmilk.publishing

import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.api.provider.ValueSourceParameters

interface GitOperationValueSourceParameters : ValueSourceParameters {
    val gitDir: DirectoryProperty
    val gitExecutable: Property<String>
}