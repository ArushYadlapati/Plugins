plugins {
    id("org.gradle.toolchains.foojay-resolver-convention").version("1.0.0")
}

includeBuild("DairyPublishing")
includeBuild("BuildMetaData")

includeBuild("FtcRobotController")

includeBuild("EasyAutoLibraries")
includeBuild("FTCLibraries")
includeBuild("FTCProjects")

includeBuild("JVMProjects")
