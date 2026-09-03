package dev.frozenmilk.libs

import dev.frozenmilk.FTC
import dev.frozenmilk.easyautolibraries.EasyAutoDependency
import dev.frozenmilk.easyautolibraries.EasyAutoScope

@Suppress("unused", "PropertyName", "FunctionName")
class SDK(ftc: FTC) : EasyAutoScope<SDK>(ftc) {
    var version = "11.2.0"

    private val sdkModule = dependency { name ->
        EasyAutoDependency(
            group = "org.firstinspires.ftc",
            artifact = name,
            defaultVersion = { version },
        )
    }

    private var androidVersionCodesAndNames = mutableMapOf<String, Pair<Int, String>>()

    init {
        androidVersionCodesAndNames["10.1.0"] = 56 to "10.1"
        androidVersionCodesAndNames["10.1.1"] = 57 to "10.1.1"
        androidVersionCodesAndNames["10.2.0"] = 58 to "10.2"
        androidVersionCodesAndNames["10.3.0"] = 59 to "10.3"
        androidVersionCodesAndNames["11.0.0"] = 60 to "11.0"
        androidVersionCodesAndNames["11.1.0"] = 61 to "11.1"
        androidVersionCodesAndNames["11.2.0"] = 62 to "11.2"
    }

    fun androidVersionCodeAndName(version: String, code: Int, name: String) {
        androidVersionCodesAndNames[version] = code to name
    }

    fun androidVersionCodeAndName(version: String) =
        requireNotNull(androidVersionCodesAndNames[version]) {
            "cannot produce version code and name for $version"
        }

    val FtcRobotController by dependency {
        EasyAutoDependency(
            group = "com.qualcomm.ftcrobotcontroller",
            artifact = "FtcRobotController",
            defaultVersion = { version },
        )
    }

    val FtcCommon by sdkModule
    val RobotCore by sdkModule
    val RobotServer by sdkModule
    val Hardware by sdkModule
    val Vision by sdkModule
    val Inspection by sdkModule
    val Blocks by sdkModule
    val OnBotJava by sdkModule

    val appcompat by dependency {
        EasyAutoDependency(
            group = "androidx.appcompat",
            artifact = "appcompat",
            version = "1.2.0",
        )
    }

    fun TeamCode() {
        runtimeOnly(FtcRobotController)
        implementation(FtcCommon)
        implementation(RobotCore)
        implementation(RobotServer)
        implementation(Hardware)
        implementation(Vision)
        implementation(Inspection)
        runtimeOnly(Blocks)
        implementation(OnBotJava)
        implementation(appcompat)
    }

    fun TeamCode(version: String) {
        this.version = version
        TeamCode()
    }
}