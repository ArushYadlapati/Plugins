run {
    val publishFTCLibraries = gradle//
        .includedBuild("FTCLibraries")//
        .task(":publishToMavenLocal")
    val publishFTCProjects = gradle//
        .includedBuild("FTCProjects")//
        .task(":publishToMavenLocal")
    val publishJVMProjects = gradle//
        .includedBuild("JVMProjects")//
        .task(":publishToMavenLocal")

    tasks.register("publishFTCLibrariesLocal") {
        description = "Publish ftc libraries and downstream to maven local"
        dependsOn(publishFTCLibraries)
        dependsOn(publishFTCProjects)
        dependsOn(publishJVMProjects)
    }
}

run {
    val publishFTCLibraries = gradle//
        .includedBuild("FTCLibraries")//
        .task(":publishAllPublicationsToDairyRepository")
    val publishFTCProjects = gradle//
        .includedBuild("FTCProjects")//
        .task(":publishAllPublicationsToDairyRepository")
    val publishJVMProjects = gradle//
        .includedBuild("JVMProjects")//
        .task(":publishAllPublicationsToDairyRepository")

    tasks.register("publishFTCLibrariesDairy") {
        description = "Publish ftc libraries and downstream to repo.dairy.foundation"
        dependsOn(publishFTCLibraries)
        dependsOn(publishFTCProjects)
        dependsOn(publishJVMProjects)
    }
}

run {
    val buildBuildMetaData = gradle//
        .includedBuild("BuildMetaData")//
        .task(":build")
    val buildDairyPublishing = gradle//
        .includedBuild("DairyPublishing")//
        .task(":build")
    val buildEasyAutoLibraries = gradle//
        .includedBuild("EasyAutoLibraries")//
        .task(":build")
    val buildFTCLibraries = gradle//
        .includedBuild("FTCLibraries")//
        .task(":build")
    val buildFTCProjects = gradle//
        .includedBuild("FTCProjects")//
        .task(":build")
    val buildJVMProjects = gradle//
        .includedBuild("JVMProjects")//
        .task(":build")
    val buildFtcRobotController = gradle//
        .includedBuild("FtcRobotController")//
        .task(":build")

    tasks.register("build") {
        description = "build all linked libraries"
        dependsOn(buildBuildMetaData)
        dependsOn(buildDairyPublishing)
        dependsOn(buildEasyAutoLibraries)
        dependsOn(buildFTCLibraries)
        dependsOn(buildFTCProjects)
        dependsOn(buildJVMProjects)
        dependsOn(buildFtcRobotController)
    }
}