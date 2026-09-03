pluginManagement {
	repositories {
		gradlePluginPortal()
		mavenCentral()
		google()
	}

	includeBuild("../DairyPublishing")
}

includeBuild("../EasyAutoLibraries") {
	dependencySubstitution {
		substitute(module("dev.frozenmilk:EasyAutoLibraries")).using(project(":"))
	}
}

plugins {
	id("org.gradle.toolchains.foojay-resolver-convention").version("1.0.0")
}
