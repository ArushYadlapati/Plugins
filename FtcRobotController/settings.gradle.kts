pluginManagement {
	repositories {
		gradlePluginPortal()
		mavenCentral()
		google()
        maven("https://repo.dairy.foundation/releases")
	}

	includeBuild("../FTCLibraries")
	includeBuild("../DairyPublishing")
}

plugins {
	id("org.gradle.toolchains.foojay-resolver-convention").version("1.0.0")
}
