pluginManagement {
	repositories {
		gradlePluginPortal()
		mavenCentral()
		google()
		maven("https://repo.dairy.foundation/releases")
	}

	includeBuild("../DairyPublishing")
}

includeBuild("../FTCLibraries") {
    dependencySubstitution {
        substitute(module("dev.frozenmilk:FTCLibraries")).using(project(":"))
    }
}

plugins {
	id("org.gradle.toolchains.foojay-resolver-convention").version("1.0.0")
}
